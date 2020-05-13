package com.cheuks.bin.original.common.cache.local;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.common.cache.local
 * @Description:淘汰指标参数
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-05-13 09:20
 *
 *
 */
public class EliminateIndicators {

    /***
     * 组数若满足条件，直接移除
     */
    private boolean remove;

    /***
     * 时间范围内命中少于此数值
     */
    private int hit;

}
