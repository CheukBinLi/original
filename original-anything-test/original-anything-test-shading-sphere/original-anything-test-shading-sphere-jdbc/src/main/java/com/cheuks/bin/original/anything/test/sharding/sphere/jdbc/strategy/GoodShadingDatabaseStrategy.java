package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 10:06
 *
 *
 */
public class GoodShadingDatabaseStrategy implements ComplexKeysShardingAlgorithm, Comparable{

    DBMatch dbMatch = new DBMatch();

    @Override
    public Collection<String> doSharding(Collection collection, ComplexKeysShardingValue complexKeysShardingValue) {
        Map<String, Range<?>> range = complexKeysShardingValue.getColumnNameAndRangeValuesMap();
        Map<String, List<?>> sharding = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        List result = new ArrayList<>();
//        if (sharding.get("id").equals("0")) {
//        result.add("ds" + (((Long) sharding.get("id").get(0)) == 0L ? "0" : "1"));
//        }
        List<Long> list = (List<Long>) sharding.get("id");
       return dbMatch.matchDB(list, collection);
//        return result;
    }


    public Collection<String> filter() {


        return null;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
