package controller;

import model.Meeting;
import model.Sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;

public class CancelMeeting extends Command {
	
	private Meeting meeting;
	private JSONArray command_array;
	
	public CancelMeeting(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for cancel meeting command");
			return;
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "meeting-id" :
					meeting.setMeetingId(value);
					break;							
				default :
					System.out.println("invalid arguments : " + name + "for adding meeting");
					break;
			}			
		}
		
		if ( meeting.getMeetingId() != null ) {
			if (!cancelMeetingInfo(this.meeting)) {
				System.out.println("Cancel meeting (meetID : "+meeting.getMeetingId()+") failed");
			}
		}
		
		//viewprint();		
	}
	
	public boolean cancelMeetingInfo(Meeting minfo) {
		
		boolean bSuccess = false;
		
		String delMeetQuery = "DELETE FROM TB_MEETING WHERE meetID = ? ";
		
		String delAttendeeQuery = "DELETE FROM TB_ATTENDEE WHERE meetID = ? ";
		
		Sql db = new Sql();		
		db.setQuery(delMeetQuery);
		db.setParameter(1, minfo.getMeetingId());		
		int n = db.write(); 
		if (n > 0) {
			db.setQuery(delAttendeeQuery);
			db.setParameter(1, minfo.getMeetingId());		
			db.write();
			bSuccess = true;
		}
		db.close();
		
		return bSuccess;		
	}

}
