package com.cheuks.bin.original.common.cache.local;

import java.util.Iterator;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.common.cache.local
 * @Description: 内存淘汰策略
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-05-13 09:15
 *
 *
 */
public interface MemoryEliminationStrategy<V> {

    /***
     * 执行器顺序：值越少越靠前，从0开始
     * @return
     */
    default int getOrder() {
        return 0;
    }

    ;

    /***
     * 策略命名
     * @return
     */
    String getTypeName();

    /***
     * 当前序列
     * @return
     */
    default MemoryEliminationStrategy<V> next() {
        return null;
    }

    default MemoryEliminationStrategy<V> setNext(MemoryEliminationStrategy<V> strategy) {
        return this;
    }

    /***
     * 当前是否已是终点
     * @return
     */
    default boolean isEnd() {
        return true;
    }

    default MemoryEliminationStrategy<V> setEnd(boolean end) {
        return this;
    }

    default boolean doProcess(Iterator<V> it, EliminateIndicators indicators) {
        Boolean result = process(it, indicators);
        if (null == result)
            return false;
        else if (result || isEnd())
            return result;
        return next().doProcess(it, indicators);
    }

    /***
     *
     * @param it 数据单元
     * @param indicators 淘汰指标参数
     * @return 是否需要被加淘汰队列，进一步处理:
     * ----------------------------------返回false将继续运行一下处理器.
     * ----------------------------------返回true直接返回。
     */
    boolean process(Iterator<V> it, EliminateIndicators indicators);

}
