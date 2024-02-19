package mtadelays;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseHelper {
	private static final String URL = "jdbc:mysql://localhost:3306/";
	private static final String USER = "root";
	private static final String PASSWORD = "sql-pwd";
	
	
	public static void createDatabase() {
		String databaseName = "mtadelays";
		String query = "create database if not exists " + databaseName + "; ";
		String query2 = "use " + databaseName + ";";
				
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
            
            PreparedStatement pstmt2 = conn.prepareStatement(query2);
            pstmt2.executeUpdate();
            
            System.out.println("DB creation successful");
        } catch (Exception e) {
        	System.out.println("DB creation failed.");
            e.printStackTrace();
        }
	}
	
	/**
	 * Creates the station table. Will replace the station table
	 * if it already exists.
	 * Throws an exception when CREATE TABLE fails.
	 */
	public static void createStationsTable() {
		String query = "CREATE TABLE IF NOT EXISTS Stations ("
				+ "stopID		varchar(5)	PRIMARY KEY,"
				+ "line			varchar(3)	PRIMARY KEY,"
				+ "stopName		varchar(40),"
				+ "borough		char(1),"
				+ "direction	varchar(40)"
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
	
	/**
	 * Inserts a row into the Stations table. Will try to create stations table
	 * if it does not already exist.
	 * @param stopID
	 * @param line
	 * @param stopName
	 * @param borough
	 * @param direction
	 */
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
	
	public static void main(String[] args) {
		createDatabase();
		addStation("R01N", "N", "Astoria-Ditmars Blvd", "Q", "Last Stop");
	}
}