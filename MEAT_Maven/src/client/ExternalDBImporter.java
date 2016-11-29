package client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.CommonUtil;
import common.SysConfig;
import model.Sql;
/**
 * This class is used for loading external db file(json) into MEAT local database 
 * @author group7
 */
public class ExternalDBImporter {
	
	public boolean[] importStatus = {false, false};
/*	
	public static void main(String[] args) {		
		ExternalDBImporter ee = new ExternalDBImporter();
		// 1. Sync employeeTable with jsonFile
		ee.updateEmployeeTable();		
		// 2. Sync roomTable with jsonFile
		ee.updateRoomTable();
	}
*/	
	/**
	 *  External DB file(json) import into MEAT DB
	 *  json file (\\resource\\employees.json)
	 *  MEAT table : TB_EMPLOYEE
	 */
	public void updateEmployeeTable()
	{
		String jsonData = CommonUtil.loadJsonFile(SysConfig.employeeJsonFile);		
		if (CommonUtil.nullTrim(jsonData).length()> 0) {			
			
			JSONParser parser = new JSONParser(); 	
			Sql db = new Sql();
			
			try {							
				JSONArray jsonArray = (JSONArray) parser.parse(jsonData);					
				for (int i=0;i<jsonArray.size();i++) {						
					if (i == 0) {
						String delQuery = "DELETE FROM TB_EMPLOYEE ";
						db.setQuery(delQuery);
						db.write();
					}					
					JSONObject jsonObject = (JSONObject) jsonArray.get(i); 	
					String employeeID = (String) jsonObject.get("id");
					String firstName = (String) jsonObject.get("firstName");
					String lastName = (String) jsonObject.get("lastName");
					String email = (String) jsonObject.get("email");
					String title = (String) jsonObject.get("title");
					long   totalVacationDays = (Long) jsonObject.get("totalVacationDays");					
					
					// Insert 
					String insQuery = "INSERT INTO TB_EMPLOYEE VALUES (?,?,?,?,?,?) ";
					db.setQuery(insQuery);
					db.setParameter(1, employeeID);
					db.setParameter(2, firstName);
					db.setParameter(3, lastName);
					db.setParameter(4, email);
					db.setParameter(5, title);
					db.setParameter(6, totalVacationDays);
					db.write();					
				}	
				importStatus[0] = true;
				System.out.println("## EmployeeDB Sync Success");
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}		
	}
	
	/**
	 *  External DB file(json) import into MEAT DB
	 *  json file (\\resource\\room.json)
	 *  MEAT table : TB_ROOM
	 */
	public void updateRoomTable()
	{
		String jsonData = CommonUtil.loadJsonFile(SysConfig.roomJsonFile);		
		if (CommonUtil.nullTrim(jsonData).length()> 0) {			
			JSONParser parser = new JSONParser(); 	
			Sql db = new Sql();
			try {				
				JSONArray jsonArray = (JSONArray) parser.parse(jsonData);				
				for (int i=0;i<jsonArray.size();i++) {							
					if (i == 0) {
						String delQuery = "DELETE FROM TB_ROOM ";
						db.setQuery(delQuery);
						db.write();
					}	
					
					JSONObject jsonObject = (JSONObject) jsonArray.get(i); 	
					String roomID = (String) jsonObject.get("id");
					String building = (String) jsonObject.get("building");
					long   floor = (Long) jsonObject.get("floor");					
					long   occupancy = (Long) jsonObject.get("occupancy");					
					// Database updating
					
					// Insert 
					String insQuery = "INSERT INTO TB_ROOM VALUES (?,?,?,?) ";
					db.setQuery(insQuery);
					db.setParameter(1, roomID);
					db.setParameter(2, building);
					db.setParameter(3, floor);
					db.setParameter(4, occupancy);
					db.write();		
				}	
				importStatus[1] = true;
				System.out.println("## RoomDB Sync Success");
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}		
	}

}
