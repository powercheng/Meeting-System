package controller;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import common.SysConfig;
import common.TimeConflictException;
import model.Employee;
import model.Meeting;
import model.Room;
import model.Sql;
/**
 * Gethering and analyzing passing add-meeting information and save into database
 * @author group7
 */
public class AddMeeting extends Command {
	private Meeting meeting;
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public AddMeeting(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
	}
	/**
	 * analyze incoming commands and check validity, finally save meeting information into database
	 */
	@Override
	public String execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No arguments for adding meeting command");
			return "failed";
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "date" :
					if(checkDateValid(value)){
						meeting.setDate(value);
						break;
					} else {						
						System.out.println("invalid date ("+value+") for adding meeting command");
						return SysConfig.fail;
					}
				case "start-time" :					
					if(checkTimeValid(value)){
						meeting.setStartTime(value);
						break;
					} else {
						System.out.println("invalid time("+value+") for adding meeting command");
						return SysConfig.fail;
					}
				case "end-time" :
					if(checkTimeValid(value)){
						meeting.setEndTime(value);
						break;
					} else {
						System.out.println("invalid time ("+value+") for adding meeting command");
						return SysConfig.fail;
					}
				case "room-id" :
					if(checkRoomIdValid(value)){
						meeting.setRoomId(value);					
						break;
					} else {
						System.out.println("invalid room id ("+value+") for adding meeting command");
						return SysConfig.fail;
					}
				case "description" :					
					if(checkStrLenValid(value)){
						meeting.setDescription(value);				
						break;
					} else {
						System.out.println("description is too long for adding meeting command");
						return SysConfig.fail;
					}
				case "attendee" :					
					if(checkEmpolyeeIdValid(value)){
						meeting.addAttendee(value);				
						break;
					} else {
						return SysConfig.fail;
					}
				default :
					System.out.println("invalid arguments : " + name + "for adding meeting");
					return SysConfig.fail;
			}			
		}
		
		if(checkMeetingArgument()) {			
			/* check room and employee schedule */
			if (!ableToAttendWithoutConflict()) {
				return SysConfig.fail;
			}			
			/* Database saving */			
			String meetingID = CommonUtil.getNextMeetID();
			meeting.setMeetingId(meetingID);
			if (!insertMeetingInfo(this.meeting)) {
				//System.out.println("Add meeting is failed");
				return SysConfig.fail;
			} else {
				return SysConfig.success;
			}
			//viewprint();
		} else {			
			return SysConfig.fail;
		}		
		
	}
	/**
	 * check if necessary variables are valid for adding a meeting
	 * @return
	 */
	private boolean checkMeetingArgument() {
		// TODO Auto-generated method stub
		if(meeting.getAttendee() == null && meeting.getAttendee().isEmpty() ||
		   meeting.getDate() == null && meeting.getDate().isEmpty()	||
		   meeting.getDescription() == null && meeting.getDescription().isEmpty() ||
		   meeting.getClass() == null && meeting.getDate().isEmpty() ||
		   meeting.getStartTime() == null && meeting.getAttendee().isEmpty() ||
		   meeting.getEndTime() == null && meeting.getEndTime().isEmpty())
		{
			System.out.println("Missing argumets for adding meeting command");
			return false;
		} else if(!checkTimeConflict(meeting.getStartTime(),meeting.getEndTime())){
		//	System.out.println(meeting.getStartTime());
		//	System.out.println(meeting.getEndTime());
			System.out.println("end time before start time");
			return false;
		} else {
			return true;
		}
		
	}
	/**
	 * check if meeting date has a conflict with room schedules, holiday, and attendee's schedules
	 * @return
	 */
	public boolean ableToAttendWithoutConflict() {
		
		// 1. first check room available
		Room room = new Room();
		room.setRoomID(meeting.getRoomId());
		
		try {
			room.roomAvailable(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
		} catch (TimeConflictException tce) {
			tce.printStackTrace();
			return false;			
		}
		// 2. check holiday
		AddHoliday holi = new AddHoliday();		
		try {
			holi.checkAvailableWithHoliday(meeting.getDate());			
		} catch (TimeConflictException tce) {
			tce.printStackTrace();
			return false;			
		}		
		// 3. check all attendees' meeting and vacation date
		LinkedList<String> attendList = meeting.getAttendee();
		
		for (int i=0;i<attendList.size();i++) {
			
			Employee emp = new Employee();
			emp.setEmployeeID((String) attendList.get(i));			

			/*check meeting*/			
			try {
				emp.checkAvailableWithMeeting(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
			} catch (TimeConflictException tce) {
				tce.printStackTrace();
				return false;
			}			
			
			/*check vacation*/
			try { 
				emp.checkAvailableWithVacation(meeting.getDate());
			} catch (TimeConflictException tce) {
				tce.printStackTrace();
				return false;
			}			
		}
		// No error !
		return true;
		
	}
	/**
	 * Insert final checked meeting information into database
	 * @param minfo
	 * @return
	 */
	public boolean insertMeetingInfo(Meeting minfo) {
		
		boolean bSuccess = false;	
		
		String insMeetQuery = "INSERT INTO TB_MEETING(meetID, meetDATE, startTIME, endTIME, description, roomID) "
							+ " VALUES (?, ?, ?, ?, ?, ?)";
		
		String insAttendeeQuery = "INSERT INTO TB_ATTENDEE(meetID, employeeID) "
							+ " VALUES (?, ?)";
		
		Sql db = new Sql();		
		db.setQuery(insMeetQuery);
		db.setParameter(1, minfo.getMeetingId());
		db.setParameter(2, minfo.getDate());
		db.setParameter(3, minfo.getStartTime());
		db.setParameter(4, minfo.getEndTime());
		db.setParameter(5, minfo.getDescription());
		db.setParameter(6, minfo.getRoomId());
		int n = db.write();
		/* if insMeetQuery is successful */
		if (n > 0) {
			LinkedList<String> attendList = minfo.getAttendee();
			for (int i=0;i<attendList.size();i++) {
				db.setQuery(insAttendeeQuery);
				db.setParameter(1, minfo.getMeetingId());
				db.setParameter(2, (String) attendList.get(i));
				db.write();
			}
			bSuccess = true;
		}
		db.close();
		
		return bSuccess;		
	}
}
