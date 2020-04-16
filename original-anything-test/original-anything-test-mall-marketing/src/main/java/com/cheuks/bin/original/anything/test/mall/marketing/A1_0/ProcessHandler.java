package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import java.util.Collection;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.mall.marketing
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-16 11:23
 *
 *
 */
public interface ProcessHandler {

    void doProcess(Collection<Goods> goodses, ProcessResult result, ProcessHandleChain chain);

}
