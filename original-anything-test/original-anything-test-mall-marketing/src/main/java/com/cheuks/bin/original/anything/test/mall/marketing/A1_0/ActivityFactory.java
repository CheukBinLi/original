package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.mall.marketing
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-16 11:57
 *
 *
 */
public class ActivityFactory {

    private Map<Long, Goods> GOODS = new ConcurrentHashMap<>();
    private Map<Long, Activity> ACTIVITY = new ConcurrentHashMap<>();
    private Map<String, ActivityHandler> ACTIVITY_HANDLER = new ConcurrentHashMap<>();

    public ActivityFactory append(Goods goods) {
        GOODS.put(goods.getId(), goods);
        return this;
    }

    public ActivityFactory appendGoods(Collection<Goods> goodses) {
        goodses.forEach(item -> {
            append(item);
        });
        return this;
    }

    public ActivityFactory append(Activity activity) {
        ACTIVITY.put(activity.getId(), activity);

        ActivityHandler activityHandler = activity.getHandler();
        List<Goods> goodsList = activity.getGoodsList();
        if (null != goodsList)
            appendGoods(goodsList);
        if (null != activityHandler)
            ACTIVITY_HANDLER.put(activityHandler.getType(), activityHandler);
        return this;
    }

    public ActivityFactory appendActivity(Collection<Activity> activitys) {
        activitys.forEach(item -> {
            append(item);
        });
        return this;
    }

    public ActivityFactory append(ActivityHandler handler) {
        ACTIVITY_HANDLER.put(handler.getType(), handler);
        return this;
    }

    public ProcessResult process(Set<Goods> goodsList) throws CloneNotSupportedException {

        //总价-拆扣
        ProcessResult result = new ProcessResult();

        List<Activity> activities = new ArrayList<>(goodsList.size() * 3);
        //过滤互斥活动
        List<Activity> filterActivites = new ArrayList<>();
        Map<Long, Activity> activityMap = new HashMap<>(goodsList.size() * 4);
        Goods temp;
        Activity activity1, activity2;
        for (Goods item : goodsList) {
            if ((temp = GOODS.get(item.getId())) == null) {
                continue;
            }
            //赋值
            item.setPrice(temp.getPrice());
            result.addPrice(item);
            //活动分组
            for (Activity subItem : temp.getActivityList()) {
                Activity activity;
                if (null == (activity = activityMap.get(subItem.getId()))) {
                    activityMap.put(subItem.getId(), activity = subItem.copy());
                    activities.add(activity);
                }
                activity.getGoodsList().add(item);
            }
        }

        //活动排序
        Collections.sort(activities);
//        for (int i = 0; i < activities.size(); i++) {
//            for (int j = i; j < activities.size(); j++) {
//                if (i == j || (activity1 = activities.get(i)).getCateory() <= 0 || (activity2 = activities.get(j)).getCateory() <= 0)
//                    continue;
//                if (activity1.getCateory() == activity2.getCateory()) {
//                    filterActivites.add(activity2);
//                }
//            }
//        }

        activities.removeAll(filterActivites);

        ProcessHandleChain processHandleChain = ProcessHandleChain.builder()
                .activitys(activities)
                .build();
        processHandleChain.doProcess(goodsList, result, processHandleChain);
        return result;
    }

}
