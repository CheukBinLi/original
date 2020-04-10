package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 15:24
 *
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ShardingGroup {

    private int id;
    private String name;
    private int start;
    private int end;
    private List<ShardingDatabase> databases = new ArrayList<>();

    public ShardingGroup append(ShardingDatabase... db) {
        for (ShardingDatabase item : db) {
            this.databases.add(item);
        }
        return this;
    }
//    private Map<String, ShardingDatabase> databases = new ConcurrentHashMap<>();
//
//    public ShardingGroup append(ShardingDatabase... db) {
//        for (ShardingDatabase item : db) {
//            databases.put(item.getName(), item);
//        }
//        return this;
//    }

}
