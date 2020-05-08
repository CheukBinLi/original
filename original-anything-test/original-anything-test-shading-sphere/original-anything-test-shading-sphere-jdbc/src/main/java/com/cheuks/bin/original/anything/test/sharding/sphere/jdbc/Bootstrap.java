package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc;

import com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.strategy.GoodShadingDatabaseStrategy;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.sharding.sphere.jdbc
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-09 09:07
 *
 *
 */
@Configuration
@ComponentScan({"com.cheuks.bin.original.anything.test.sharding.sphere"})
@PropertySources({@PropertySource("classpath:sharding-jdbc.properties")})
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.cheuks.bin.original.anything.test.sharding.sphere.jdbc.dao")
@EnableTransactionManagement
@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Bootstrap.class);
    }

    @Primary
    @Bean("goodShadingDatabaseStrategy")
    public ComplexKeysShardingAlgorithm createComplexKeysShardingAlgorithm() {
        return new GoodShadingDatabaseStrategy();
    }
}
