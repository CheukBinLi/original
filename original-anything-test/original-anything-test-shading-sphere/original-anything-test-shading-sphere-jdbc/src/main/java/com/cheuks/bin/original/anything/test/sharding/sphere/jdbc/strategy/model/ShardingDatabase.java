package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 15:25
 *
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ShardingDatabase {

    private int id;
    private String name;
    private int code;
    private int start;
    private int end;
    private Set<String> slaves = new CopyOnWriteArraySet<>();
    private Map<Integer, ShardingTable> tables = new ConcurrentHashMap<>();

    public ShardingDatabase append(@NonNull ShardingTable... shardingTables) {
        for (ShardingTable table : shardingTables) {
            tables.put(table.getCode(), table);
        }
        return this;
    }

    public ShardingDatabase append(@NonNull String... slaves) {
        for (String s : slaves) {
            this.slaves.addAll(Arrays.asList(slaves));
        }
        return this;
    }

}
