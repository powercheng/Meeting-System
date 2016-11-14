package controller;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;
import common.CommonUtil;
import common.SysConfig;
import model.Meeting;
import model.Room;
import model.Sql;


public class AddMeeting extends Command {
	private Meeting meeting;
	private JSONArray command_array;
	
	public AddMeeting(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for adding meeting command");
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
						System.out.println("invalid empolyee id ("+value+") for adding meeting command");
						return SysConfig.fail;
					}
				default :
					System.out.println("invalid arguments : " + name + "for adding meeting");
					return SysConfig.fail;
			}			
		}
		
		if(checkMeetingArgument()){			
			/* check room and employee schedule */
			if (!checkTimeConflict()) {
				return SysConfig.fail;
			}
			
			/* Databse writing */			
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
		} else if(checkTimeConflict(meeting.getStartTime(),meeting.getEndTime())){
			System.out.println("end time before start time");
			return false;
		} else {
			return true;
		}
		
	}
	
	public boolean checkTimeConflict() {
		
		// 1. first check room availability
		Room room = new Room();
		room.setRoomID(meeting.getRoomId());
		boolean check = room.checkRoomAvailablity(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
		if (!check) System.out.println("room("+meeting.getRoomId()+") is not available");
		
		// 2. check attendee's meeting schedule
		
		// 3. check attendee's vacation schedule
		
		return check;
		
	}
	
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
			LinkedList<String> lis = minfo.getAttendee();
			for (int i=0;i<lis.size();i++) {
				db.setQuery(insAttendeeQuery);
				db.setParameter(1, minfo.getMeetingId());
				db.setParameter(2, (String) lis.get(i));
				db.write();
			}
			bSuccess = true;
		}
		db.close();
		
		return bSuccess;		
	}
}
