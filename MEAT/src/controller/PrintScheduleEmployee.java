package controller;

import model.Employee;
import model.Meeting;
import model.Room;
import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;
import common.CommonUtil;
import common.SysConfig;

public class PrintScheduleEmployee extends Command {
	
	private Employee employee;
	private String srchStartDay;
	private String srchEndDay;
	private String outfileName;
	
	private JSONArray command_array;
	
	public PrintScheduleEmployee(JSONArray command_array) {
		super();
		this.command_array = command_array;		
		this.employee = new Employee();
	}
/*	
	public PrintScheduleEmployee() {		
		this.employee = new Employee();
	}
	
	public static void main(String[] args) {
		PrintScheduleEmployee test = new PrintScheduleEmployee();
		test.employee.getPersonInfo("bob099");
		test.setSrchStartDay("01012016");
		test.setSrchEndDay("01012017");
		test.setOutfileName("test.txt");
		test.printEmployeeSchedule();
	}
*/	
	@Override
	public String execute() {		
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for print-schedule-employee");
			return SysConfig.fail;
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
					System.out.println("invalid arguments : " + name + "for print-schedule-employee");
					return SysConfig.fail;
			}			
		}
		
		if (!checkCondition()) {
			return SysConfig.fail;
		}
		
		if (!printEmployeeSchedule()) {
			return SysConfig.fail;
		}
		
		return SysConfig.success;		
	}
	
	@SuppressWarnings("unchecked")
	public boolean printEmployeeSchedule() {
		
		JSONObject rtnObj = new JSONObject();
		Sql db = new Sql();
		String meetDetailQuery = "SELECT TA.meetID as 'meeting-id', TA.meetDATE as date, "
				+ " TA.startTIME as 'start-time', TA.endTime as 'end-time', "
				+ " TA.roomID as 'room-id', TA.description "
				+ " FROM TB_MEETING TA INNER JOIN TB_ATTENDEE TB ON TA.meetID = TB.meetID "
				+ " WHERE TB.employeeID = ? and "
				+ " substr(TA.meetDATE,5,4)||substr(TA.meetDATE,0,3)||substr(TA.meetDATE,3,2) between ? and ? ";
		
		db.setQuery(meetDetailQuery);
		db.setParameter(1, this.employee.getEmployeeID());
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
		
		/* Save json object content into file */
		return CommonUtil.saveFile(getOutfileName(), rtnObj);
		
	}
		
	public boolean checkCondition() {
		
		/* Necessary information check */
		if (employee.getEmployeeID() == null) {
			System.out.println("No employeeID for print-schedule-employee");
			return false;
		}
		if (getSrchStartDay() == null) {
			System.out.println("No start-date for print-schedule-employee");
			return false;
		}
		if (getSrchEndDay() == null) {
			System.out.println("No end-date for print-schedule-employee");
			return false;
		}
		if (getOutfileName() == null) {
			System.out.println("No output-file for print-schedule-employee");
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
