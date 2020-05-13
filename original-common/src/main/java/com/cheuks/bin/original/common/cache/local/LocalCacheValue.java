package com.cheuks.bin.original.common.cache.local;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.common.cache.local
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-05-12 16:07
 *
 *
 */
public class LocalCacheValue<V extends Serializable> implements Serializable {

    private LongAdder hit = new LongAdder();
    private long expireTime = -1;
    private V data;

    public LocalCacheValue() {
        this.hit.add(1);
    }

    public LocalCacheValue(long expireTime, V data) {
        this.expireTime = expireTime;
        this.data = data;
        this.hit.add(1);
    }

    public V getAndRefresh(long expireTime) {
        this.hit.add(1);
        this.expireTime = expireTime;
        return this.data;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public long getHit() {
        return this.hit.longValue();
    }

    public boolean isExpire() {
        return expireTime < 0 ? false : expireTime < System.currentTimeMillis();
    }

    public boolean isExpire(long refValue) {
        return expireTime < refValue;
    }

    public int getOneSecondHitCount(long refTime) {
        return Long.valueOf((this.expireTime - refTime) / 1000 / hit.intValue()).intValue();

    }

    /***
     * 过期对比参照时间内，是否已逝去XX时间
     * @param refTime 参照时间
     * @param timeLost 逝去时间
     * @return true:已满足条件，false不满足条件
     */
    public boolean isTimeLost(long refTime, long timeLost) {
        return this.expireTime - refTime <= timeLost;
    }

    public V getDate() {
        return this.data;
    }

    public static <T extends Serializable> LocalCacheValue<T> build(T t, long expireTime) {
        return new LocalCacheValue<>(expireTime, t);
    }

}
