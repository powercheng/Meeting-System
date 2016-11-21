package controller;

import model.Meeting;
import model.Sql;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.CancelMeetingException;
/**
 * This class is used to cancel the scheduled meeting
 * @author group7
 *
 */
public class CancelMeeting extends Command {
	
	private Meeting meeting;
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public CancelMeeting(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
	}
	/**
	 * analyze the passing commands and check validity, finally insert passing data into database
	 */
	@Override
	public void execute() throws CancelMeetingException {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new CancelMeetingException("No argument for cancel-meeting command");
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
					throw new CancelMeetingException("invalid arguments : " + name + "for cancel-meeting");
			}			
		}
		
		if ( meeting.getMeetingId() == null ) {
			throw new CancelMeetingException("Missing meeting id for cancel meeting command");
		}	
		if (!cancelMeetingInfo(this.meeting)) {
			throw new CancelMeetingException("cancel meeting to database is failed for cancel meeting command");
		}		
	}
	/**
	 * delete target meeting information from database
	 * @param minfo
	 * @return
	 */
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
