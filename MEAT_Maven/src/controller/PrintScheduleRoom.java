package controller;

import model.Room;
import model.Sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import exceptions.PrintScheduleRoomException;
/**
 * Print room's schedules onto screen or save it to the file 
 * @author group7
 *
 */
public class PrintScheduleRoom extends Command {
	private Room room;
	private String srchStartDay;
	private String srchEndDay;
	private String outfileName;
	
	private JSONArray command_array;
	/**
	 * Constructor for scripting running mode
	 * @param command_array
	 */
	public PrintScheduleRoom(JSONArray command_array) {
		super();
		this.command_array = command_array;		
		this.room = new Room();
	}
	/**
	 * default constructor for interactive mode
	 */
	public PrintScheduleRoom() {		
		this.room = new Room();
	}
/*	
	public static void main(String[] args) {
		PrintScheduleRoom test = new PrintScheduleRoom();
		test.room.getRoomInfo("3A66");
		test.setSrchStartDay("01012016");
		test.setSrchEndDay("01012017");
		test.setOutfileName("test.txt");
		test.printRoomSchedule(); 
	}
*/
	/**
	 * Handling passing commands parameter and print out the saved room schedules
	 */
	@Override
	public void execute() throws PrintScheduleRoomException {		
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new PrintScheduleRoomException("No arguments for print-schedule-room");
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "room-id" :
					if(checkRoomIdValid(value)) { 
						room.getRoomInfo(value);
						break;
					} else {
						throw new PrintScheduleRoomException("invalid arguments : " + name + "for print-schedule-room");
					}
				case "start-date" :
					if(checkDateValid(value)){
						setSrchStartDay(value);
						break;
					} else {
						throw new PrintScheduleRoomException("invalid arguments : " + name + "for print-schedule-room");
					}
				case "end-date" :
					if(checkDateValid(value)){
						setSrchEndDay(value);
						break;
					} else {
						throw new PrintScheduleRoomException("invalid arguments : " + name + "for print-schedule-room");
					}
				case "output-file" :
					setOutfileName(value);
					break;
					
				default :
					throw new PrintScheduleRoomException("invalid arguments : " + name + "for print-schedule-room");
			}			
		}
		
		checkCondition();		
		if (!printFileRoomSchedule()) {
			throw new PrintScheduleRoomException("save file failed for print-schedule-room");
		}	
			
	}
	/**
	 * Fetching room schedules from the database with specific range of time
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getRoomScheduleList() {
		
		JSONObject rtnObj = new JSONObject();
		Sql db = new Sql();
		String meetDetailQuery = "SELECT meetID as 'meeting-id', meetDATE as date, "
				+ " startTIME as 'start-time', endTime as 'end-time', "
				+ " roomID as 'room-id', description FROM TB_MEETING WHERE roomID = ? AND "
				+ " substr(meetDATE,5,4)||substr(meetDATE,0,3)||substr(meetDATE,3,2) between ? and ? ";
		db.setQuery(meetDetailQuery);
		db.setParameter(1, this.room.getRoomID());
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
	 * Print fectched result onto screen
	 */
	/*
	public void printScreenRoomSchedule() {
		
		if (getSrchStartDay() == null || getSrchEndDay() == null) {
			System.out.println("Not set search time interval");
			return;
		}
		
		JSONObject rTobObj = getRoomScheduleList();
    	JSONArray  rMeetList = (JSONArray) rTobObj.get("events");
    	
    	System.out.println("#Current scheduled meetings for the room("+room.getRoomID()+") between "+getSrchStartDay()+" and "+getSrchEndDay()+" #");
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
    	
    	if (rMeetList.size() == 0) System.out.println(" No scheduled meeting for "+room.getRoomID()+"");
    	
    	System.out.println("------------------------------------------------------------------------------------------------");
		
	}
*/
	/**
	 * Save the fetched result into file (json)
	 * @return
	 */
	public boolean printFileRoomSchedule() {
		
		JSONObject rtnObj = getRoomScheduleList();
		/* Save json object content into file */
		return CommonUtil.saveFile(getOutfileName(), rtnObj);
		
	}
	/**
	 * check validity of variables 	
	 * @return
	 */
	public boolean checkCondition() {
		
		/* Necessary information check */
		if (room.getRoomID() == null) {
			System.out.println("No roomID for print-schedule-room");
			return false;
		}
		if (getSrchStartDay() == null) {
			System.out.println("No start-date for print-schedule-room");
			return false;
		}
		if (getSrchEndDay() == null) {
			System.out.println("No end-date for print-schedule-room");
			return false;
		}
		if (getOutfileName() == null) {
			System.out.println("No output-file for print-schedule-room");
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

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
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
