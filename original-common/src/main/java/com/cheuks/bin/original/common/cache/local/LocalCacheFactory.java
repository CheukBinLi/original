package com.cheuks.bin.original.common.cache.local;

import com.cheuks.bin.original.common.cache.CacheException;
import com.cheuks.bin.original.common.cache.CacheFactory;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.common.from.local
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-05-12 16:06
 *
 *
 */
public class LocalCacheFactory<V extends Serializable> implements CacheFactory<String, V> {

    private Map<String, LocalCacheValue<V>> from = new ConcurrentHashMap<>();
    private Map<String, LocalCacheValue<V>> to = new ConcurrentHashMap<>();
    private List<MemoryEliminationStrategy<V>> memoryEliminationStrategy = new ArrayList<>();
    final Comparator<MemoryEliminationStrategy<V>> ORDER_SORT = new Comparator<MemoryEliminationStrategy<V>>() {
        @Override
        public int compare(MemoryEliminationStrategy<V> o1, MemoryEliminationStrategy<V> o2) {
            return o2.getOrder() < o1.getOrder() ? 1 : o1.equals(o2) ? 0 : -1;
        }
    };
    /***
     * 策略头
     */
    private MemoryEliminationStrategy head = null;
    private volatile boolean suspend = false;
    private volatile boolean init = false;

    /***
     * 缓存数据缓存时间，默认一分钟
     */
    private volatile long interval = 6000;

    private volatile long checkInterval = 20000;
    /***
     * 缓存数据量
     */
    private final int MAX_SIZE;
    private final int FROM, TO;
    /***
     * 2秒移量
     */
    private volatile long offset = 2000;
    /***
     * 每秒命中率:默认5,小于此值时被视为不活跃
     */
    private volatile int minHitConut = 5;

    private EliminateWorker fromRunnable;
    private EliminateWorker toRunnable;

    ExecutorService executor = new ThreadPoolExecutor(2, 2, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));

    interface Worker extends Runnable {
        void wakeup();
    }

    public LocalCacheFactory(int MAX_SIZE) {
        this.MAX_SIZE = MAX_SIZE;
        this.FROM = Long.valueOf(MAX_SIZE * 7 / 10).intValue();
        this.TO = MAX_SIZE - FROM;
    }

    public long getNextExprie() {
        return System.currentTimeMillis() + this.interval + this.offset;
    }

    @Override
    public V take(String key) throws CacheException {
        LocalCacheValue data = from.get(key);
        if (null == data) {
            if (to.size() < 1) {
                return null;
            }
            data = to.get(key);
        }
        return null == data ? null : (V) data.getAndRefresh(getNextExprie());
    }

    @Override
    public V put(String key, V value) throws CacheException {
        if (null == value)
            return null;
        Map<String, LocalCacheValue<V>> continer = from;
        if (from.size() >= FROM) {
            continer = to;
            fromRunnable.wakeup();
        }
        if (to.size() >= TO) {
            toRunnable.wakeup();
            return value;
        }
        continer.put(key, LocalCacheValue.build(value, getNextExprie()));
        return value;
    }

    @Override
    public V remove(String key) throws CacheException {
        LocalCacheValue<V> result0 = from.remove(key), result1 = to.remove(key);
        return (V) (null == result0 ? to.remove(key) : result0);
    }

    @Override
    public void scriptClear() throws CacheException {

    }

    @Override
    public int size() throws CacheException {
        return from.size() + to.size();
    }

    @Override
    public Set<String> keys() throws CacheException {
        return (Set<String>) CollectionUtil.collageCollection(from.keySet(), to.keySet());
    }

    @Override
    public Collection<V> values() throws CacheException {
        return (Collection<V>) CollectionUtil.collageCollection(from.values(), to.values());
    }

    @Override
    public synchronized void dectory() {
        if (null != executor && !executor.isShutdown() && !this.suspend) {
            if (null != fromRunnable)
                fromRunnable.shutdown();
            if (null != toRunnable)
                toRunnable.shutdown();
            executor.shutdown();
        }
    }

    @Override
    public synchronized void init() {
        if (this.init)
            return;
        this.init = true;
        executor.submit(fromRunnable = new EliminateWorker(from, to, head, interval, checkInterval, offset, minHitConut));
        executor.submit(toRunnable = new EliminateWorker(to, from, head, interval, checkInterval, offset, minHitConut) {
            @Override
            public void after(final Map from, final Map to, final Iterator it) {

                if (to.size() < from.size()) {
                    to.putAll(from);
                    from.clear();
                }
            }
        });
        resortStrategy();
    }

    public LocalCacheFactory<V> appendStrategy(MemoryEliminationStrategy<V> strategy) {
        memoryEliminationStrategy.add(strategy);
        if (!init) {
            resortStrategy();
        }
        return this;
    }

    public synchronized void resortStrategy() {
        if (CollectionUtil.isEmpty(this.memoryEliminationStrategy))
            return;
        Collections.sort(memoryEliminationStrategy, ORDER_SORT);
        MemoryEliminationStrategy temp = null;
        for (MemoryEliminationStrategy item : memoryEliminationStrategy) {
            if (temp == null) {
                temp = item;
                continue;
            }
            temp.setNext(item.setEnd(true)).setEnd(false);
        }
        this.head = memoryEliminationStrategy.get(0);
    }

    public static void main(String[] args) throws CacheException, InterruptedException {
        final LocalCacheFactory l = new LocalCacheFactory(10);
        l.init();
        Thread.sleep(2000);
        for (int i = 0; i < 8; i++) {
            l.put("" + i, i);
        }
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    for (; ; ) {
                        l.take("9");
                        l.take("4");
                        System.err.println(l.size());
                        Thread.sleep(1000);
                        System.out.println(i++);
                        if (i == 25) {
                            l.fromRunnable.wakeup();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();

    }
}
