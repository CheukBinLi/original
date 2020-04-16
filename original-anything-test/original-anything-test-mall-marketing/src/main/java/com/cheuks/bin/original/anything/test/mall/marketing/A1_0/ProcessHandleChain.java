package com.cheuks.bin.original.anything.test.mall.marketing.A1_0;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
 * @date 2020-04-16 11:24
 *
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProcessHandleChain implements ProcessHandler {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    List<Activity> activitys = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    Activity currentlActivity;

    private int index = 0;

    public void doProcess(Collection<Goods> goodses, ProcessResult result, ProcessHandleChain chain) {
        if (null == this.activitys || this.activitys.size() <= index)
            return;
        (currentlActivity = activitys.get(index++)).getHandler().doProcess(goodses, null == result ? new ProcessResult() : result, this);
    }

    public ProcessHandleChain addHandle(Activity activitie) {
        activitys.add(activitie);
        return this;
    }

    public void sort() {
        Collections.sort(this.activitys);
    }

}
