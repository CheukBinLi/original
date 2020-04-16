package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
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
 * @date 2020-04-16 09:58
 *
 *
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResult {

    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private List<ActivityGoods> activityGoodses = new ArrayList<>();//包含的活动

    public ProcessResult append(ActivityGoods activityGoods) {
        activityGoodses.add(activityGoods);
        this.discount = this.discount.add(activityGoods.getDiscount());
        return this;
    }

    public ProcessResult addPrice(Goods goods) {
        this.total = this.total.add(goods.getPrice().multiply(new BigDecimal(goods.getQuantity())));
        return this;
    }

}
