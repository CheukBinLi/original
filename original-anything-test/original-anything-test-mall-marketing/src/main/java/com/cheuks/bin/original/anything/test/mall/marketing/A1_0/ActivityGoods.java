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
 * @date 2020-04-16 09:59
 *
 *
 */

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGoods {

    private int id;
    private String type;
    private String name;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    List<Goods> detail = new ArrayList<>();

    public ActivityGoods calculate(final Goods goods) {
        detail.add(goods);
        this.total = this.total.add(goods.getPrice().multiply(BigDecimal.valueOf(goods.getQuantity())));
        return this;
    }

    public ActivityGoods calculate(BigDecimal discount) {
        this.discount = this.discount.add(discount);
        return this;
    }

}
