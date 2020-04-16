package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
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
public class Activity implements Serializable, Comparable<Activity>, Cloneable {

    private long id;
    private long cateory;//活动分类:双11_01,双11_02互斥
    private String name;
    private int order;//越小越靠前：最大0
    private String type;
    private BigDecimal quantity;//可买数量
    private BigDecimal discount;
    private List<Goods> goodsList = new LinkedList();
    private ActivityHandler handler;//处理器


    @Override
    public int compareTo(Activity o) {
        return o.order - this.order;
    }

    @Override
    public Activity clone() throws CloneNotSupportedException {
        return (Activity) super.clone();
    }

    public Activity copy() {
        return Activity.builder()
                .id(this.id)
                .cateory(this.cateory)
                .name(this.name)
                .order(this.order)
                .type(this.type)
                .quantity(this.quantity)
                .discount(this.discount)
                .handler(this.handler)
                .goodsList(new LinkedList())
                .build();
    }

    public Activity append(Goods goods) {
        goodsList.add(goods);
        return this;
    }

}
