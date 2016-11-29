package model;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
/**
 * Meeting model class
 * @author group7
 *
 */
public class Meeting {
	private String meetingId;
	private String date;
	private String startTime;
	private String endTime;
	private String roomId;
	private LinkedList<String> attendee;
	private String description;
	
	/**
	 * Default constructor 
	 */
	public Meeting() {
		super();
		this.attendee = new LinkedList<String>();
	}
	
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public LinkedList<String> getAttendee() {
		return attendee;
	}
	public void addAttendee(String str) {
		String regex = ",|\\s+";
        String[] attendees = str.split(regex);
        for(String attendee : attendees){
        	/* if blank or null attendee contains, continues */
			if (CommonUtil.nullTrim(attendee).equals("")) 
				continue;
        	this.attendee.add(attendee);
        }		
	} 
	public void setAttendee(LinkedList<String> attendee) {
		this.attendee = attendee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Get the stored meeting information and set the fetched information into this class
	 * @param meetID
	 */
	public void getMeetingInfo(String meetID) {
		
		if (meetID != null && meetID.length() > 0) {
			
			Sql db = new Sql();
			String infoMeetQuery = "SELECT meetID, meetDATE, startTIME, endTIME, description, roomID"
					+ " FROM TB_MEETING WHERE meetID = ? ";
			db.setQuery(infoMeetQuery);
			db.setParameter(1, meetID);
			JSONArray recArr = db.read();		
			for (int i=0; i<recArr.size(); i++) {
				// In reality, every time just one record
				JSONObject rsObj = (JSONObject) recArr.get(i);
				/* Set personal information */
				setMeetingId((String) rsObj.get("meetID"));
				setDate((String) rsObj.get("meetDATE"));
				setStartTime((String) rsObj.get("startTIME"));
				setEndTime((String) rsObj.get("endTIME"));
				setDescription((String) rsObj.get("description"));
				setRoomId((String) rsObj.get("roomID"));
			}	
			
			if (recArr.size() == 0) {
				System.out.println("No such meeting scheduled");
			}
			
			/* Attendees getting */
			String infoAttendeeQuery = "SELECT meetID, employeeID FROM TB_ATTENDEE WHERE meetID = ? ";
			db.setQuery(infoAttendeeQuery);
			db.setParameter(1, meetID);
			JSONArray attArr = db.read();
			for (int i=0;i<attArr.size();i++) {
				JSONObject rsObj = (JSONObject) attArr.get(i);
				this.attendee.add((String) rsObj.get("employeeID"));
			}			
			db.close();
		} else {
			System.out.println("meetingID is not passed");
		}
	}
	
	/**
	 * Print this meeting information onto the screen
	 */
/*	
	public void printCurrentMeetingInfo() {
		
		System.out.println("#MeetID   #Meeting Time             #RoomID    #Description           #AttendeeID ");
    	System.out.println("------------------------------------------------------------------------------------------------");
    	
		String attendString = "";
		for (int k=0;k<attendee.size();k++) {			
			attendString += (String) attendee.get(k);
			if (k != attendee.size()-1) {  // last list
				attendString += ",";
			}
		}
		
		System.out.println("# " + getMeetingId() + "       " + CommonUtil.dateFormat(getDate(),"MMddyyyy","MM.dd.yyyy") 
		+ " " + getStartTime() + "-" + getEndTime() + "     " + getRoomId() 
		+ "       " + CommonUtil.blankPadding(getDescription(), 18) + "    " + attendString);
    	
	}
*/	
}
