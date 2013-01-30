package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccessor {
	protected static Connection conn = null;
	protected static String url = "jdbc:mysql://localhost:3306/";
	protected static String dbName = "dungeon";
	protected static String driver = "com.mysql.jdbc.Driver";
	protected static String userName = "root";
	protected static String password = "osznpe5h";

	
	/**
	 * Connects to the database
	 */
	public static void connect(){
		try{
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName, userName, password);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Disconnects from the database
	 */
	public static void disconnect(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}