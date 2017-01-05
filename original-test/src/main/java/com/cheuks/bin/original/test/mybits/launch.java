package com.cheuks.bin.original.test.mybits;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class launch {

	public static void main(String[] args) {
		// mybatis的配置文件
		String resource = "mybits/mybatis.xml";
		// 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
		// 构建sqlSession的工厂
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		// 使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
		// Reader reader = Resources.getResourceAsReader(resource);
		// 构建sqlSession的工厂
		// SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		// 创建能执行映射文件中sql的sqlSession
		SqlSession session = sessionFactory.openSession();
		/**
		 * 映射sql的标识字符串， me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值， getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
		 */
		String statement = "com.cheuks.bin.original.test.mybits.TestMybitsModelMapper.getTest";// 映射sql的标识字符串
		String insert = "com.cheuks.bin.original.test.mybits.TestMybitsModelMapper.insertTest";
		// 执行查询返回一个唯一user对象的sql
		// User user = session.selectOne(statement, 1);
		TestMybitsModel testMybitsModel = new TestMybitsModel();
		testMybitsModel.setName("GOOD");
		session.insert(insert, testMybitsModel);
		session.commit();

		testMybitsModel = session.selectOne(statement, 2);

		System.out.println(testMybitsModel.getName());

	}

}
