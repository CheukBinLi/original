package com.cheuks.bin.original.test.scan;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import com.cheuks.bin.original.common.util.scan.Scan;
import com.cheuks.bin.original.common.util.scan.ScanSimple;

public class T1 {

	public static void main(String[] args) throws Throwable {
//		org.springframework.orm.hibernate5.LocalSessionFactoryBean
//		org.springframework.orm.hibernate5.HibernateTransactionManager
		PropertyConfigurator.configure(T1.class.getResource("/").getPath() + "log4j.properties");
		Scan scan = new ScanSimple();
		Map<String, Set<String>> s=scan.doScan("rmi.*$xml,com.*.binxx.*.test");
		System.out.println(s);
	}

}
