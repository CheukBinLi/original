package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.mall.marketing
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-16 10:07
 *
 *
 */
public class ActivityFullSubtractHandler implements ActivityHandler {
    @Override
    public String getType() {
        return "FULL_SUBTRACT";
    }

    private static final BigDecimal TWO = new BigDecimal(2);

    @Override
    public void doProcess(Collection<Goods> goodses, ProcessResult result, ProcessHandleChain chain) {
        Activity activity = chain.getCurrentlActivity();
        List<Goods> list = activity.getGoodsList();
        ActivityGoods activityGoods = new ActivityGoods();
        for (Goods item : list) {
            activityGoods.calculate(item);
        }
        if (activityGoods.getTotal().compareTo(activity.getQuantity()) > 0) {
            activityGoods.calculate(activity.getDiscount());
        }
        result.append(activityGoods);
        chain.doProcess(goodses, result, chain);
    }
}
