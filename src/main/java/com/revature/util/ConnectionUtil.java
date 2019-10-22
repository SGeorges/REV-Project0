package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	public static Connection getConnection() {
		try {
			String url = System.getenv("GOSBANK_URL");

			Connection conn = DriverManager.getConnection(
								url,
								System.getenv("BM_ROLE"),
								System.getenv("BM_PASS"));
			return conn;					
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unable to connect to database.");
			return null;
		}
	}

}
