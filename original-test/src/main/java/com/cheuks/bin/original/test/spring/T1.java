package com.cheuks.bin.original.test.spring;

import com.cheuks.bin.original.test.DoThing;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.test.spring
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-04-03 17:01
 *
 *
 */
public class T1 {

    public static void main(String[] args) {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.setResourceLoader(new DefaultResourceLoader());
        reader.loadBeanDefinitions("application-config.xml");
        System.out.println(Arrays.toString(factory.getBeanDefinitionNames()));
        factory.getBean(DoThing.class);
//        Connection.TRANSACTION_READ_COMMITTED

        final DataSource ds = new DriverManagerDataSource();

        TransactionTemplate template = new TransactionTemplate();
        template.setTransactionManager(new DataSourceTransactionManager(ds));
        template.execute((TransactionStatus status)->{
            Connection con = DataSourceUtils.getConnection(ds);
            return null;
        });

    }

}
