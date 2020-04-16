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
public class ActivityHalfPriceHandler implements ActivityHandler {
    @Override
    public String getType() {
        return "HALF_PRICE";
    }

    private static final BigDecimal TWO = new BigDecimal(2);

    @Override
    public void doProcess(Collection<Goods> goodses, ProcessResult result, ProcessHandleChain chain) {
        Activity activity = chain.getCurrentlActivity();
        List<Goods> list = activity.getGoodsList();
        BigDecimal total = BigDecimal.ZERO, discount = BigDecimal.ZERO;
        ActivityGoods activityGoods = new ActivityGoods();
        for (Goods item : list) {
            if (item.getQuantity() >= activity.getQuantity().intValue()) {
                activityGoods.calculate(item.getPrice().divide(TWO));
            }
            activityGoods.calculate(item);
        }

        result.append(activityGoods);
        chain.doProcess(goodses, result, chain);
    }
}
