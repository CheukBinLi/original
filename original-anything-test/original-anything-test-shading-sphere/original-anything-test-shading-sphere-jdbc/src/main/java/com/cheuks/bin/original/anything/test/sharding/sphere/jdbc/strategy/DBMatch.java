package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy;

import com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model.ShardingDatabase;
import com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.model.ShardingGroup;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 15:19
 *
 *
 */
@Service
public class DBMatch {

    private CopyOnWriteArrayList<ShardingGroup> shardingGroup;
    private Map<String, ShardingDatabase> shardingDatabase;
    private volatile int datases = 0;

    ReentrantLock lock = new ReentrantLock();


    public final DBMatch init() throws Throwable {
        boolean beLock = false;
        try {
            if (!(beLock = lock.tryLock())) {
                return this;
            }
            shardingGroup = new CopyOnWriteArrayList<>();
            shardingGroup.add(
                    new ShardingGroup(1, "group_0", 0, 10, new ArrayList<>())
                            .append(
                                    ShardingDatabase.builder().id(1).name("master0").start(0).end(5).build(),
                                    ShardingDatabase.builder().id(2).name("master1").start(6).end(10).build()
                            )
            );
            datases = 2;
        } catch (Exception e) {
            throw new Throwable(e);
        } finally {
            if (beLock)
                lock.unlock();
        }
        return this;
    }

    public final Collection<String> matchDB(Collection<Long> ids, Collection<String> logicDataBase) {
        int status;
        for (status = datases; ; ) {
            if (status == 0) {
                try {
                    init();
                    status = datases;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else if (status < 0) {
                synchronized (this) {
                    try {
                        this.wait(100);
                    } catch (InterruptedException e) {
                    }
                }
            } else {
                break;
            }
        }
        Set<String> result = new HashSet<>();
//        for (Long id : ids) {
//            //筛选群组
//            for (ShardingGroup group : shardingGroup) {
//                if (id <= group.getEnd() && id >= group.getStart()) {
//                    //数据库定位
//                    for (ShardingDatabase database : group.getDatabases()) {
//                        if (id <= database.getEnd() && id >= database.getStart()) {
//                            result.add(database.getName());
//                            if (result.size() >= dataseLen) {
//                                return result;
//                            }
//                        }
//                    }
//                }
//            }
//        }
        for (Long id : ids) {
            //筛选群组
            for (ShardingGroup group : shardingGroup) {
                if (id <= group.getEnd() && id >= group.getStart()) {
                    //数据库定位
                    for (ShardingDatabase database : group.getDatabases()) {
                        if (id <= database.getEnd() && id >= database.getStart()) {
                            result.add(database.getName());
                            if (result.size() >= this.datases) {
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

}
