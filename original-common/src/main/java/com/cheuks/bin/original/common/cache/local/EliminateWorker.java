package com.cheuks.bin.original.common.cache.local;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.common.cache.local
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-05-13 16:38
 *
 *
 */
public class EliminateWorker<V extends Serializable> implements Runnable {

    private final Map<String, LocalCacheValue<V>> from;
    private final Map<String, LocalCacheValue<V>> to;

    /***
     * 策略头
     */
    private final MemoryEliminationStrategy head;
    private volatile boolean suspend = false;

    /***
     * 缓存数据缓存时间，默认一分钟
     */
    private volatile long interval = 6000;

    private volatile long checkInterval = 20000;
    /***
     * 2秒移量
     */
    private volatile long offset = 2000;
    /***
     * 每秒命中率:默认5,小于此值时被视为不活跃
     */
    private volatile int minHitConut = 5;

    static Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);


    public synchronized void wakeup() {
        this.notify();
    }

    public synchronized void shutdown() {
        this.suspend = true;
    }

    public void before(Map<String, LocalCacheValue<V>> from, Map<String, LocalCacheValue<V>> to) {
    }

    public void onProcess(Map<String, LocalCacheValue<V>> from, Map<String, LocalCacheValue<V>> to, Iterator<Map.Entry<String, LocalCacheValue<V>>> it) {
    }


    public void after(Map<String, LocalCacheValue<V>> from, Map<String, LocalCacheValue<V>> to, Iterator<Map.Entry<String, LocalCacheValue<V>>> it) {
    }

    @Override
    public void run() {
        Map.Entry<String, LocalCacheValue<V>> item;
        LocalCacheValue<V> value;
        long now, hitTime;
        Iterator<Map.Entry<String, LocalCacheValue<V>>> it;
        while (!this.suspend) {
            try {
                synchronized (this) {
                    do {
                        this.wait(this.checkInterval);
                    }
                    while (CollectionUtil.isEmpty(this.from));
                }
                before(from, to);
                it = this.from.entrySet().iterator();
                now = System.currentTimeMillis() + this.offset;
                hitTime = this.interval * 7 / 10;
                while (it.hasNext()) {
                    item = it.next();
                    onProcess(from, to, it);
                    if (null == this.head) {
                        value = item.getValue();
                        if (null == value)
                            continue;
                        if (value.isExpire(now)) {
                            it.remove();
                        } else if (value.isTimeLost(now, hitTime) && value.getOneSecondHitCount(now) < minHitConut) {
                            //缓存末访问70%时间
                            //此时间内命中少于minHitConut
                            it.remove();
                        }
                    } else {
                        head.doProcess(it, null);
                    }
                    after(from, to, it);
                }
            } catch (Throwable e) {
                LOG.info("{} daemon is shutdown. msg:{}", Thread.currentThread().getName(), e.getLocalizedMessage());
                return;
            }
        }
    }

    public EliminateWorker(final Map<String, LocalCacheValue<V>> from, final Map<String, LocalCacheValue<V>> to,
                           final MemoryEliminationStrategy head) {
        this.from = from;
        this.to = to;
        this.head = head;
    }

    public EliminateWorker(final Map<String, LocalCacheValue<V>> from, final Map<String, LocalCacheValue<V>> to,
                           final MemoryEliminationStrategy head, final long interval, final long checkInterval, final long offset,
                           final int minHitConut) {
        this.from = from;
        this.to = to;
        this.head = head;
        this.interval = interval;
        this.checkInterval = checkInterval;
        this.offset = offset;
        this.minHitConut = minHitConut;
    }

    public EliminateWorker<V> setSuspend(boolean suspend) {
        this.suspend = suspend;
        return this;
    }

    public EliminateWorker<V> setInterval(long interval) {
        this.interval = interval;
        return this;
    }

    public EliminateWorker<V> setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
        return this;
    }

    public EliminateWorker<V> setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public EliminateWorker<V> setMinHitConut(int minHitConut) {
        this.minHitConut = minHitConut;
        return this;
    }

    public Map<String, LocalCacheValue<V>> getFrom() {
        return from;
    }

    public Map<String, LocalCacheValue<V>> getTo() {
        return to;
    }

    public MemoryEliminationStrategy getHead() {
        return head;
    }

    public boolean isSuspend() {
        return suspend;
    }

    public long getInterval() {
        return interval;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public long getOffset() {
        return offset;
    }

    public int getMinHitConut() {
        return minHitConut;
    }
}
