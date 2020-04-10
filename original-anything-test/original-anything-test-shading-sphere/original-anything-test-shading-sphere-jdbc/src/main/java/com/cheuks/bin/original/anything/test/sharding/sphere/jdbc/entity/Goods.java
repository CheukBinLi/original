package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 09:19
 *
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Goods implements Comparable<Goods> {

    private long id;
    private String name;


    @Override
    public int compareTo(Goods o) {
        return Long.valueOf(this.id - o.id).intValue();
    }
}


