package client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import common.CommonUtil;
import common.TimeConflictException;
import model.Employee;
import model.Sql;
/**
 * To check single functionality, test run 
 * @author group7
 *
 */
public class TestRun {

	public static void main(String[] args) {
		
		TestRun run = new TestRun();
		//run.testMID();
		//run.sqlSelectTest();
		//run.sqlInsertTest();
		//run.testUUID();
		run.initDB();
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
				String firstName = (String) rsObj.get("totalVACATIONDAY");
				System.out.println(firstName);
			}
			System.out.println(arr.toString());
			db.close();  // make sure to call this method			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	/**
	 * SQLite Insert test 
	 */
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
	 * Test input data clear
	 */
	public void initDB() {
		
		try {
			String query = "DELETE FROM TB_ATTENDEE ";
			Sql db = new Sql();		
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_MEETING ";
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_VACATION ";
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_HOLIDAY ";
			db.setQuery(query);			
			db.write();
			// close connection
			db.close();  // make sure to call this method			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
		
	/**
	 *  To test making unique universal ID
	 */	
	public void testUUID() {
		
		String uid = CommonUtil.createUUID(10);
		System.out.println(uid);
	}
	
	/**
	 *  To make meeting ID
	 */	
	public void testMID() {
		
		String id = CommonUtil.getNextMeetID();
		System.out.println(id);
	}
	
}
