package com.cheuks.bin.original.test.guid;

import com.cheuks.bin.original.cache.JedisStandAloneCacheFactory;
import com.cheuks.bin.original.cache.redis.RedisLuaSimple;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;

import java.io.IOException;
import java.util.concurrent.*;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.test.guid
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-20 14:09
 *
 *
 */
public class RedisGuid {

    public static void main(String[] args) throws IOException, RedisExcecption, InterruptedException {

        final JedisStandAloneCacheFactory factory = new JedisStandAloneCacheFactory();
        factory.setHost("192.168.0.251");
        factory.setPassword("e#l2jISe*d0AdEeS29w");
//        factory.set("t1", "t1");

        final RedisLuaSimple redisLuaSimple = new RedisLuaSimple();
        redisLuaSimple.setRedisFactory(factory);
        redisLuaSimple.initLoader();
        ExecutorService executorService = new ThreadPoolExecutor(100, 150,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10));
        ExecutorService executorService1 = new ThreadPoolExecutor(100, 1000000,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10000000));
        final CountDownLatch countDownLatch = new CountDownLatch(8);
        final String time = Math.floorDiv(System.currentTimeMillis(), 1000) + "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        final CountDownLatch subCountDownLatch = new CountDownLatch(12000);
                        for (int j = 0; j < 12000; j++) {
                            executorService1.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println(factory.evalSha(redisLuaSimple.getSha("time"), 1
                                                , "ID_TEST"
                                                , "10010"
                                                , "APP"
                                                , "ORDER"
                                                , "1"
//                                                , Math.floorDiv(System.currentTimeMillis(), 1000) + ""
//                                                , time
                                        ));
                                    } catch (RedisExcecption redisExcecption) {
                                        redisExcecption.printStackTrace();
                                    } finally {
                                        subCountDownLatch.countDown();
                                    }
                                }
                            });
                        }
                        try {
                            subCountDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - start + ":" + 8 * 1200);
//        local sequenceKey = KEYS[1];
//        --tenant name / id
//        local tenantKey = ARGV[1];
//        --application name / database
//        local appKey = ARGV[2];
//        --application module name / table name
//        local module = ARGV[3];
//        --get generate of number
//        local quantity = tonumber(ARGV[4]);
//        System.out.println(factory.evalSha(redisLuaSimple.getSha("time"), 1
//                , "ID_TEST"
//                , "10010"
//                , "APP"
//                , "ORDER"
//                , "1"));
        System.exit(0);
    }

}
