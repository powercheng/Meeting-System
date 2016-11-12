package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.PreparedStatement;

import common.CommonUtil;
import common.SysConfig;
import model.Sql;

public class TestRun {

	public static void main(String[] args) {
		
		TestRun run = new TestRun();
		run.sqlSelectTest();
		//run.sqlInsertTest();
		//run.testUUID();	
	}
	
	/**
	 *  To test sqlTest method, 
	 *  we must include sqlite library (/lib/sqlLite---.jar) into this project.
	 */
	public void sqlSelectTest() {
		
		try {
			String query = "SELECT * FROM TB_EMPLOYEE WHERE employeeID = ? ";
			Sql db = new Sql();
			db.setQuery(query);			
			db.setParameter(1, "bob099");
			JSONArray arr = db.read();
			for (int i=0;i<arr.size();i++) {
				JSONObject rsObj = (JSONObject) arr.get(i);
				String firstName = (String) rsObj.get("firstNAME");
				System.out.println(firstName);
			}
			db.close();  // make sure to call this method			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	public void sqlInsertTest() {
		
		try {
			String query = "INSERT INTO TB_EMPLOYEE VALUES (?,?,?,?,?,?) ";
			Sql db = new Sql();
			// set query
			db.setQuery(query);
			// set param
			db.setParameter(1, "bob0991");
			db.setParameter(2, "cathy");
			db.setParameter(3, "Jim");
			db.setParameter(4, "test@gmail.com");
			db.setParameter(5, "supervisor");
			db.setParameter(6, 5);
			// store data
			db.write();
			// close connection
			db.close();  // make sure to call this method			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
		
	/**
	 *  To make unique universal ID
	 */	
	public void testUUID() {
		
		String uid = CommonUtil.createUUID(10);
		System.out.println(uid);
	}
	
	
}
