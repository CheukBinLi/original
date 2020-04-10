package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.service;

import com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.dao.GoodsDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.service
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 11:29
 *
 *
 */
@Service
public class GoodsService implements InitializingBean {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        long id = Integer.valueOf(new Random().nextInt(10)).longValue();
        System.out.println(id);
//        goodsDao.save(id);
        goodsDao.save(0);
        goodsDao.save(1);
        goodsDao.save(2);
        goodsDao.save(3);
        goodsDao.save(4);
        goodsDao.save(5);
        goodsDao.save(6);
        goodsDao.save(7);
        goodsDao.save(8);
        goodsDao.save(9);
        goodsDao.save(10);
        goodsDao.save(11);
//        System.out.println(goodsDao.getX(0L, 1L, 10L));
        System.out.println(goodsDao.getX(0L, 1L, 10L));
    }

}
