package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
public class Goods implements Cloneable {

    private long id;
    private String name;
    private int quantity = 1;
    private BigDecimal price = BigDecimal.ZERO;
    //    private BigDecimal total = BigDecimal.ZERO;
//    private BigDecimal discount = BigDecimal.ZERO;//当前价格
    private List<Activity> activityList = new ArrayList<>();

    @Override
    public Goods clone() throws CloneNotSupportedException {
        return (Goods) super.clone();
    }

//    public Goods calculate() {
//        this.total = this.total.multiply(new BigDecimal(this.quantity)).subtract(discount);
//        return this;
//    }

    public Goods append(Activity activity) {
        boolean canAppend = true;
        if (this.activityList.size() > 0 && activity.getCateory() > 0) {
            for (Iterator<Activity> it = this.activityList.iterator(); it.hasNext(); ) {
                Activity item = it.next();
                if (item.getCateory() == activity.getCateory()) {
                    canAppend = false;
                    if (item.getOrder() > activity.getOrder()) {
                        it.remove();
                        this.activityList.add(activity);
                        break;
                    } else if (item.getOrder() == activity.getOrder()) {
//                        LoggerFactory.getLogger(Goods.class).error("you can check activity has conflict[{}]:[{}],current use [{}].", item.getId(), activity.getId(), item.getId());
                        System.err.println(String.format("you can check activity has conflict[%d]:[%d],current use [%d].", item.getId(), activity.getId(), item.getId()));
                    }
                    break;
                }
            }
        }
        if (canAppend)
            this.activityList.add(activity);
        return this;
    }
}
