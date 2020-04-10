package com.cheuks.bin.original.anything.test.jdbc.test;

import com.cheuks.bin.original.anything.test.jdbc.test.sharding.model.ShardingContext;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class T2 {
    public static void main(String[] args) throws SQLException {

        ShardingContext shardingContext = new ShardingContext();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername("root");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/demo_ds_0?characterEncoding=utf-8");
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setPassword("123456");

        shardingContext.getDataSources().put("first", basicDataSource);

        BasicDataSource basicDataSource2 = new BasicDataSource();
        basicDataSource2.setUsername("root");
        basicDataSource2.setUrl("jdbc:mysql://localhost:3306/demo_ds_1?characterEncoding=utf-8");
        basicDataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource2.setPassword("123456");
        shardingContext.getDataSources().put("second", basicDataSource2);

        Iterator<DataSource> it = shardingContext.getDataSources().values().iterator();
        Connection connection = it.next().getConnection();
        Connection connection2 = it.next().getConnection();

        String table = "create table if not exists t1 (id BIGINT AUTO_INCREMENT, name VARCHAR(50)  NULL, SEX VARCHAR(50), PRIMARY KEY (id))";

        connection.createStatement().execute(table);
        connection2.createStatement().execute(table);

        connection.setAutoCommit(false);
        connection2.setAutoCommit(false);

        try {
            Statement s1 = connection.createStatement();
            Statement s2 = connection2.createStatement();
            s1.execute("INSERT INTO `t1` (`name`, `SEX`) VALUES ('小明', '男');");
            s2.execute("INSERT INTO `t1` (`name`, `SEX`) VALUES ('小明', '男');");
            if (2 > 0)
                throw new Exception("xxxxx");
            connection.commit();
            connection2.commit();
        } catch (Exception e) {
            connection.rollback();
            connection2.rollback();
        }

    }
}
