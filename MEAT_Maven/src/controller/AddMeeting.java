package controller;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import exceptions.TimeConflictException;
import exceptions.AddMeetingException;
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
	public void execute() throws AddMeetingException {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new AddMeetingException("No arguments for adding meeting command");
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
						throw new AddMeetingException("invalid date ("+value+") for adding meeting command");												
					}
				case "start-time" :					
					if(checkTimeValid(value)){
						meeting.setStartTime(value);
						break;
					} else {
						throw new AddMeetingException("invalid time("+value+") for adding meeting command");
					}
				case "end-time" :
					if(checkTimeValid(value)){
						meeting.setEndTime(value);
						break;
					} else {
						throw new AddMeetingException("invalid time ("+value+") for adding meeting command");
					}
				case "room-id" :
					if(checkRoomIdValid(value)){
						meeting.setRoomId(value);					
						break;
					} else {
						throw new AddMeetingException("invalid room id ("+value+") for adding meeting command");
					}
				case "description" :					
					if(checkStrLenValid(value)){
						meeting.setDescription(value);				
						break;
					} else {
						throw new AddMeetingException("description is too long for adding meeting command");
					}
				case "attendee" :					
					if(checkEmpolyeeIdValid(value)){						
						meeting.addAttendee(value);	
						break;
					} else {
						throw new AddMeetingException("invalid attendee id ("+value+") for adding meeting command");
					}
				default :
					throw new AddMeetingException("invalid arguments : " + name + "for adding meeting");
			}			
		}
		
		if(!checkMeetingArgument()) {			
			throw new AddMeetingException("Missing argumets for adding meeting command");
		} 
		if(!checkTimeConflict(meeting.getStartTime(),meeting.getEndTime())){
			throw new AddMeetingException("end time before start time");
		}
		ableToAttendWithoutConflict();
		
		String meetingID = CommonUtil.getNextMeetID();
		meeting.setMeetingId(meetingID);
		if (!insertMeetingInfo(this.meeting)) {
			throw new AddMeetingException("adding meeting to database is failed for adding meeting command");
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
			return false;
		} else {
			return true;
		}
		
	}
	
	/**
	 * check if meeting date has a conflict with room schedules, holiday, and attendee's schedules
	 * @return
	 */
	public void ableToAttendWithoutConflict() throws AddMeetingException {
		
		// 1. first check room available
		Room room = new Room();
		room.setRoomID(meeting.getRoomId());
		
		try {
			room.roomAvailable(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
		} catch (TimeConflictException tce) {
			throw new AddMeetingException(tce.getMessage() + "for adding meeting command");
		}
		// 2. check holiday
		AddHoliday holi = new AddHoliday();		
		try {
			holi.checkAvailableWithHoliday(meeting.getDate());			
		} catch (TimeConflictException tce) {
			throw new AddMeetingException(tce.getMessage() + "for adding meeting command");			
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
				throw new AddMeetingException(tce.getMessage() + "for adding meeting command");
			}			
			
			/*check vacation*/
			try { 
				emp.checkAvailableWithVacation(meeting.getDate());
			} catch (TimeConflictException tce) {
				throw new AddMeetingException(tce.getMessage() + "for adding meeting command");
			}			
		}		
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
