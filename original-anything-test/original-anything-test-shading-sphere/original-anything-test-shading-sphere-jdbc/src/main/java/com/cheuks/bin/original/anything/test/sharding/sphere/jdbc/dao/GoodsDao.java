package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.dao;

import com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.entity.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.dao
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 09:29
 *
 *
 */
@Mapper
@Service
public interface GoodsDao {

    @Select("SELECT * FROM goods WHERE id in(#{id},#{id1},#{id2}) limit 0,1")
    List<Goods> getX(@Param("id") Long id, @Param("id1") Long id1, @Param("id2") Long id2);

    @Transactional
    @Insert("insert into goods(`id`,`name`) values(#{id},'${id}商品')")
    int save(@Param("id") long id);

}
