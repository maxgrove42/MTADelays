package mtadelays;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseHelper {
	private static String DEFAULT_URL = "jdbc:mysql://localhost:3306/mtadelays";
	private static String DEFAULT_USER = "root";
	private static String DEFAULT_PASSWORD = "sql-pwd";
	
	private String url;
	private String user;
	private String password;
	
	/**
	 * Default constructor for DatabaseHelper
	 */
	DatabaseHelper() {
		this.url = DEFAULT_URL;
		this.user = DEFAULT_USER;
		this.password = DEFAULT_PASSWORD;
	}
	
	/**
	 * Constructor for DatabaseHelper to use custom connection information
	 * @param url
	 * @param user
	 * @param password
	 */
	DatabaseHelper(String url, String user, String password) {
		this.url = DEFAULT_URL;
		this.user = DEFAULT_USER;
		this.password = DEFAULT_PASSWORD;
	}
	
	public void executeQuery(String query) {
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
        	System.out.println("DB creation failed.");
            e.printStackTrace();
        }
	}
	
	/*

	public static void createStationsTable() {
		String query = "CREATE OR REPLACE TABLE Stations ("
				+ "stopID		varchar(5),"
				+ "line			varchar(3),"
				+ "stopName		varchar(40),"
				+ "borough		char(1),"
				+ "direction	varchar(40),"
				+ "PRIMARY KEY (stopID, line)"
				+ ");";
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.executeUpdate();
            System.out.println("Insert successful");
        } catch (Exception e) {
        	System.out.println("Station create table failed.");
            e.printStackTrace();
        }
	}
	

	public static void addStation(String stopID, String line,
			String stopName, String borough, String direction) {
		
		//try to create the stations table if it does not already exist.
		createStationsTable();
		
		
		String query = "INSERT INTO Stations (stopID, line, stopName, borough, direction)"
				+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	       PreparedStatement pstmt = conn.prepareStatement(query)) {
	
	       pstmt.setString(1, stopID);
	       pstmt.setString(2, line);
	       pstmt.setString(3, stopName);
	       pstmt.setString(4, borough);
	       pstmt.setString(5, direction);
	       
	       pstmt.executeUpdate();
	       System.out.println("Insert of [" + stopID + ", " + line + "] successful");
	
	   } catch (Exception e) {
	       System.out.println("Insert of [" + stopID + ", " + line + "] failed.");
	       e.printStackTrace();
	   }	
	}
	*/
	public static void main(String[] args) {
	}
}