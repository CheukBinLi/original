package com.cheuks.bin.original.anything.test.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.Test;

public class FirstTest {

	@Test
	public void connectionTest() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo_ds", "root", "123456");
			Statement statement = conn.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS JDBC_DEMO (id BIGINT AUTO_INCREMENT, order_id BIGINT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (id));");

			PreparedStatement preparedStatement = conn.prepareStatement("insert into JDBC_DEMO (order_id,user_id,status) values(?,?,?)");
			preparedStatement.setObject(1, 110);
			preparedStatement.setObject(2, 120);
			preparedStatement.setObject(3, "shit");
			preparedStatement.execute();
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
