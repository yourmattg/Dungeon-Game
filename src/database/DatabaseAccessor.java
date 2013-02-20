package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccessor {
	protected static Connection conn = null;
	protected static String url = "";
	protected static String dbName = "";
	protected static String driver = "";
	protected static String userName = "";
	protected static String password = "";

	
	/**
	 * Connects to the database
	 */
	public static void connect(){
		return;
		/*try{
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName, userName, password);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Disconnects from the database
	 */
	public static void disconnect(){
		return;
		/*try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}