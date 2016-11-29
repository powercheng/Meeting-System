package controller;

import model.Employee;
import model.Sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import exceptions.PrintScheduleEmployeeException;
/**
 * Print employee's scheduled meeting within specific range of date or save it into file 
 * @author group7
 *
 */
public class PrintScheduleEmployee extends Command {
	
	private Employee employee;
	private String srchStartDay;
	private String srchEndDay;
	private String outfileName;
	
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public PrintScheduleEmployee(JSONArray command_array) {
		super();
		this.command_array = command_array;		
		this.employee = new Employee();
	}
	/**
	 * default constructor 
	 */
	public PrintScheduleEmployee() {		
		this.employee = new Employee();
	}
/*
	public static void main(String[] args) {
		PrintScheduleEmployee test = new PrintScheduleEmployee();
		test.employee.getPersonInfo("bob099");
		test.setSrchStartDay("01012016");
		test.setSrchEndDay("01012017");
		test.setOutfileName("test.txt");
		test.printEmployeeSchedule();
	}
*/	
	/**
	 * Gethering an employee's meeting schedules and print the result into file typed json
	 */
	@Override
	public void execute() throws PrintScheduleEmployeeException{		
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new PrintScheduleEmployeeException("No arguments for print-schedule-employee");
		}
		
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "employee-id" :
					employee.getPersonInfo(value);
					break;	
				case "start-date" :
					if(checkDateValid(value)){
						setSrchStartDay(value);
						break;
					} else {
						throw new PrintScheduleEmployeeException("invalid arguments : " + name + "for print-schedule-employee");
					}
				case "end-date" :
					if(checkDateValid(value)){
						setSrchEndDay(value);
						break;
					} else {
						throw new PrintScheduleEmployeeException("invalid arguments : " + name + "for print-schedule-employee");
					}
				case "output-file" :
					setOutfileName(value);
					break;
					
				default :
					throw new PrintScheduleEmployeeException("invalid arguments : " + name + "for print-schedule-employee");
			}			
		}
		
		checkCondition();		
		if (!printFileEmployeeSchedule()) {
			throw new PrintScheduleEmployeeException("save file failed for print-schedule-employee");
		}	
	}
	/**
	 * Fetching employee's meeting schedules in specific range of time	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getEmployeeScheduleList() {
		
		JSONObject rtnObj = new JSONObject();
		Sql db = new Sql();
		String meetDetailQuery = "SELECT TA.meetID as 'meeting-id', TA.meetDATE as date, "
				+ " TA.startTIME as 'start-time', TA.endTime as 'end-time', "
				+ " TA.roomID as 'room-id', TA.description "
				+ " FROM TB_MEETING TA INNER JOIN TB_ATTENDEE TB ON TA.meetID = TB.meetID "
				+ " WHERE TB.employeeID = ? and "
				+ " substr(TA.meetDATE,5,4)||substr(TA.meetDATE,0,3)||substr(TA.meetDATE,3,2) between ? and ? ";
		
		db.setQuery(meetDetailQuery);
		db.setParameter(1, this.getEmployee().getEmployeeID());
		db.setParameter(2, CommonUtil.dateFormat(getSrchStartDay(),"MMddyyyy","yyyyMMdd"));
		db.setParameter(3, CommonUtil.dateFormat(getSrchEndDay(),"MMddyyyy","yyyyMMdd"));
		
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
	 * Print fetched employee's schedules onto screen 
	 */
/*	
	public void printScreenEmployeeSchedule() {
		
		if (getSrchStartDay() == null || getSrchEndDay() == null) {
			System.out.println("Not set search time interval");
			return;
		}
		
		JSONObject rTobObj = getEmployeeScheduleList();
    	JSONArray  rMeetList = (JSONArray) rTobObj.get("events");
    	
    	System.out.println("#Current scheduled meetings for the employee("+getEmployee().getEmployeeID()+") between "+getSrchStartDay()+" and "+getSrchEndDay()+" #");
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
    	
    	if (rMeetList.size() == 0) System.out.println(" No scheduled meeting for "+this.getEmployee().getEmployeeID()+"");
        	
    	System.out.println("------------------------------------------------------------------------------------------------");
		
	}
*/	
	/**
	 * save fetched employee's schedules into json file 
	 */
	public boolean printFileEmployeeSchedule() {
		
		JSONObject jsonObj = getEmployeeScheduleList();		
		/* Save json object content into file */
		return CommonUtil.saveFile(getOutfileName(), jsonObj);
		
	}
	/**
	 * check passing variables validity	
	 * @return
	 */
	public void checkCondition() throws PrintScheduleEmployeeException{
		
		/* Necessary information check */
		if (employee.getEmployeeID() == null) {
			throw new PrintScheduleEmployeeException("No employeeID for print-schedule-employee");
		}
		if (getSrchStartDay() == null) {
			throw new PrintScheduleEmployeeException("No start-date for print-schedule-employee");
		}
		if (getSrchEndDay() == null) {
			throw new PrintScheduleEmployeeException("No end-date for print-schedule-employee");
		}
		if (getOutfileName() == null) {
			throw new PrintScheduleEmployeeException("No output-file for print-schedule-employee");
		}		
		
	}
	
	public String getSrchStartDay() {
		return srchStartDay;
	}

	public void setSrchStartDay(String srchStartDay) {
		this.srchStartDay = srchStartDay;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
