package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.mall.marketing
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-16 11:56
 *
 *
 */
public class bootstrap {

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println(2024 & 3);
        Date d = new Date(1410856598);
        long time = Math.floorDiv(System.currentTimeMillis(), 1000) + 28800;
        System.err.println(time % 86400);
        System.err.println("Math.floorMod:" + Math.floorMod(time, 86400));
        System.err.println(Math.floorMod(time, 86400) / 3600);
        System.err.println((time % 86400) / 3600);
        System.out.println(Long.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE);
        System.err.println((time % 86400) % 3600);
        System.err.println(3600 - ((time % 86400) % 3600));
        System.err.println((3600 - ((time % 86400) % 3600)) / 60);

        LocalDateTime a = LocalDateTime.now();
        int year = a.getDayOfYear();
        int year1 = a.getYear();
        a.getHour();
        a.getDayOfYear();
        d.getYear();
        ActivityFactory factory = new ActivityFactory();

        Goods testProduct;
        Goods testProduct1;
        Activity activity = new Activity();
        activity
                .setCateory(1)
                .setOrder(2)
                .setId(110).setQuantity(BigDecimal.valueOf(5))
                .append(
                        testProduct = new Goods()
                                .append(activity)
                                .setId(8001)
                                .setName("测试商品")
                                .setPrice(new BigDecimal(10))
                )
                .append(
                        testProduct1 = new Goods()
                                .append(activity)
                                .setId(8002)
                                .setName("测试商品2")
                                .setPrice(new BigDecimal(10))
                )
                .setHandler(new ActivityHalfPriceHandler());
        Activity activity1 = new Activity();
        activity1
                .setOrder(1)
                .setCateory(1)
                .setId(120)
                .setQuantity(BigDecimal.valueOf(40))
                .setDiscount(BigDecimal.valueOf(30))
                .append(testProduct1.append(activity1))
                .setHandler(new ActivityFullSubtractHandler());

        factory.append(activity);
        factory.append(activity1);

        Set<Goods> goodsList = new HashSet<>();

        Goods g1 = Goods.builder()
                .id(8001)
                .quantity(6).build();
        Goods g2 = Goods.builder()
                .id(8002)
                .quantity(6).build();

        goodsList.add(g1);
        goodsList.add(g2);

        ProcessResult result = factory.process(goodsList);
        System.out.println(result.getTotal() + "  :  " + result.getDiscount());

    }

}
