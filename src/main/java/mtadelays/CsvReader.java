package mtadelays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

import java.io.FileReader;
import java.io.Reader;

public class CsvReader {
	
	/**
	 * Read in a csv from filepath and return a string of SQL INSERT statements.
	 * Utilizes insertInto from DatabaseHelper to insert.
	 * @param filepath
	 * @param tableName
	 * @return List<String> insertStatements
	 */
	public static List<String> readCSVToInsertStatements(String filepath, String tableName) {
		List<String> insertStatements = new LinkedList<String>();
		try (Reader reader = new FileReader(filepath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                String insertQuery = generateInsertQuery(tableName, record);
                System.out.println(insertQuery);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
		return insertStatements;
	}

	private static String generateInsertQuery(String tableName, CSVRecord record) {
		String output = "INSERT INTO " + tableName + " VALUES (";
		for (int i = 0; i < record.size(); i++) {
			output += record.get(i);
			if (i != record.size() - 1)
				output += ", ";
		}
		return output + ");";
			
	}
	
	public static void main(String[] args) {
		
	}

}
