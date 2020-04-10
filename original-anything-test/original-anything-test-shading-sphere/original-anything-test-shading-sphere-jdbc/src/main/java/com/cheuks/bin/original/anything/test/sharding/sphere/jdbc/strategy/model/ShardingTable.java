package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 15:35
 *
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ShardingTable {

    private int id;
    private String name;
    private int code;

}
