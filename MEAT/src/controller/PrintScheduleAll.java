package controller;

import model.Sql;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import common.CommonUtil;
import common.SysConfig;
/**
 * Print screen or into file about all company's meeting schedules in specific period
 * @author group7
 *
 */
public class PrintScheduleAll extends Command {
	
	private String srchStartDay;
	private String srchEndDay;
	private String outfileName;
	
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public PrintScheduleAll(JSONArray command_array) {
		super();
		this.command_array = command_array;	
	}
	/**
	 * default constructor for interactive mode 
	 */
	public PrintScheduleAll() {	
		super();
	}
/*	
	public static void main(String[] args) {
		PrintScheduleAll test = new PrintScheduleAll();	
		test.setSrchStartDay("01012016");
		test.setSrchEndDay("01012017");
		test.setOutfileName("test.txt");
		test.printAllCompanySchedule();
	}
*/
	/**
	 * Gethering meeting schedules and print the result into file typed json
	 */
	@Override
	public String execute() {		
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No arguments for print-schedule-all");
			return SysConfig.fail;
		}
		for(int i = 0; i < command_array.size(); i++) {
			
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			
			switch(name) {			
				case "start-date" :
					if(checkDateValid(value)){
						setSrchStartDay(value);
						break;
					} else {
						return SysConfig.fail;
					}
				case "end-date" :
					if(checkDateValid(value)){
						setSrchEndDay(value);
						break;
					} else {
						return SysConfig.fail;
					}
				case "output-file" :
					setOutfileName(value);
					break;					
				default :
					System.out.println("invalid arguments : " + name + "for print-schedule-all");
					return SysConfig.fail;
			}			
		}
		
		if (!checkCondition()) {
			return SysConfig.fail;
		}
		
		if (!printFileAllCompanySchedule()) {
			return SysConfig.fail;
		}
		
		return SysConfig.success;		
	}
	/**
	 * Fetch the result from database with time span condition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAllCompanyScheduleList() {
		
		JSONObject rtnObj = new JSONObject();
		
		Sql db = new Sql();
		String meetDetailQuery = "SELECT meetID as 'meeting-id', meetDATE as date, "
				+ " startTIME as 'start-time', endTime as 'end-time', "
				+ " roomID as 'room-id', description FROM TB_MEETING WHERE  "
				+ " substr(meetDATE,5,4)||substr(meetDATE,0,3)||substr(meetDATE,3,2) between ? and ? ";
		db.setQuery(meetDetailQuery);		
		db.setParameter(1, CommonUtil.dateFormat(getSrchStartDay(),"MMddyyyy","yyyyMMdd"));
		db.setParameter(2, CommonUtil.dateFormat(getSrchEndDay(),"MMddyyyy","yyyyMMdd"));
		
		JSONArray meetArr = db.read();
		JSONArray mergedArr = new JSONArray();
		
		for (int i=0; i<meetArr.size(); i++) {
			
			JSONObject rsetObj = (JSONObject) meetArr.get(i);		
			String meetID = (String) rsetObj.get("meeting-id");
			/*Attendees find*/
			String meetAttendeeQuery = "SELECT TA.employeeID as 'employee-id', "
					+ "ifnull(TB.firstNAME||' '||TB.lastNAME,'') as 'name' "
					+ " FROM TB_ATTENDEE TA LEFT JOIN TB_EMPLOYEE TB ON TA.employeeID = TB.employeeID "
					+ " WHERE TA.meetID = ? ";
			db.setQuery(meetAttendeeQuery);
			db.setParameter(1, meetID);
			JSONArray attendeeArr = db.read();
			
			/* Put the meeting detail json object's tail */
			rsetObj.put("attendees", attendeeArr);			
			/* Put the merged one into new mergedArr*/
			mergedArr.add(rsetObj);
		}
		rtnObj.put("events", mergedArr);
		db.close();
		
		return rtnObj;
	}
	/**
	 * Print fetching data onto current command-line screen
	 */
	public void printScreenAllCompanySchedule() {
		
		if (getSrchStartDay() == null || getSrchEndDay() == null) {
			System.out.println("Not set search time interval");
			return;
		}
		
		JSONObject rTobObj = getAllCompanyScheduleList();
    	JSONArray  rMeetList = (JSONArray) rTobObj.get("events");
    	
    	System.out.println("#Current scheduled meetings for the company between "+getSrchStartDay()+" and "+getSrchEndDay()+" #");
    	System.out.println("#MeetID   #Meeting Time             #RoomID    #Description           #AttendeeID(NAME)");
    	System.out.println("------------------------------------------------------------------------------------------------");
    	
    	for (int i=0; i<rMeetList.size();i++) {
    		
    		JSONObject rSubObj = (JSONObject) rMeetList.get(i);
    		String saveMID = (String) rSubObj.get("meeting-id");
    		String saveDate = (String) rSubObj.get("date");
    		String saveSTime = (String) rSubObj.get("start-time");
    		String saveETime = (String) rSubObj.get("end-time");
    		String saveRID   = (String) rSubObj.get("room-id");
    		String saveDESC   = (String) rSubObj.get("description");
    		JSONArray attList = (JSONArray) rSubObj.get("attendees");
    		String attendString = "";
    		for (int k=0;k<attList.size();k++) {
    			JSONObject attObj = (JSONObject) attList.get(k);
    			attendString += (String) attObj.get("employee-id") +"("+(String) attObj.get("name")+")";
    			if (k != attList.size()-1) {  // last list
    				attendString += ",";
    			}
    		}
    		System.out.println("# " + saveMID + "       " + CommonUtil.dateFormat(saveDate,"MMddyyyy","MM.dd.yyyy") 
    								+ " " + saveSTime + "-" + saveETime + "     " + saveRID 
    								+ "       " + CommonUtil.blankPadding(saveDESC, 18) + "    " + attendString);
    	}
    	
    	System.out.println("------------------------------------------------------------------------------------------------");
		
	}
	/**
	 * Save fetched result into the json file
	 * @return
	 */
	public boolean printFileAllCompanySchedule() {
		
		JSONObject jsonObj = getAllCompanyScheduleList();		
		/* Save json object content into file */
		return CommonUtil.saveFile(getOutfileName(), jsonObj);
		
	}
	/**
	 * Check validity of all variables condition	
	 * @return
	 */
	public boolean checkCondition() {
		
		if (getSrchStartDay() == null) {  // NULL ALLOWED
			setSrchStartDay(SysConfig.minDay);
		}
		if (getSrchEndDay() == null) { // NULL ALLOWD
			setSrchStartDay(SysConfig.maxDay);
		}
		if (getOutfileName() == null) {
			System.out.println("No output-file for print-schedule-all");
			return false;
		}		
		
		return true;
	}
	
	public String getSrchStartDay() {
		return srchStartDay;
	}

	public void setSrchStartDay(String srchStartDay) {
		this.srchStartDay = srchStartDay;
	}

	public String getSrchEndDay() {
		return srchEndDay;
	}

	public void setSrchEndDay(String srchEndDay) {
		this.srchEndDay = srchEndDay;
	}

	public String getOutfileName() {
		return outfileName;
	}

	public void setOutfileName(String outfileName) {
		this.outfileName = outfileName;
	}

}
