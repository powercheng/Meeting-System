package controller;

import model.Employee;
import model.Meeting;
import model.Room;
import model.Sql;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import common.SysConfig;
import exceptions.EditMeetingException;
import exceptions.TimeConflictException;

/**
 * Edit and change the scheduled meeting information
 * @author group7
  */
public class EditMeeting extends Command {
	
	private Meeting meeting;
	private JSONArray command_array;
	private String atteedeeOption;  //ADD or REMOVE 
	private LinkedList<String> checkSkipedAttendeeList;  // unchanged attendees no need to check schedules 
	
	/* Changing category tracking */
	boolean timeChanged = false;
	boolean roomChanged = false;
	boolean attendeeChanged = false;
	/**
	 * Constructor for script running mode, particularly meeting detail  
	 * @param command_array
	 */
	public EditMeeting(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
		this.checkSkipedAttendeeList = new LinkedList<String>();  // add JUNIT
	}
	/**
	 * Constructor for script running mode, particularly attendee adding and removing 
	 * @param command_array
	 * @param attendeeOption
	 */
	public EditMeeting(JSONArray command_array, String attendeeOption) {
		super();
		this.command_array = command_array;
		this.meeting = new Meeting();
		this.atteedeeOption = attendeeOption;
		this.checkSkipedAttendeeList = new LinkedList<String>();  // add JUNIT
	}
	/**
	 * Check and verify passing commands data, and update old one into new information
	 */
	@Override
	public void execute() throws EditMeetingException {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new EditMeetingException("No arguments for edit-meeting command");
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "meeting-id" :
					if(checkMeetingIdValid(value)){
						/*current db information setting */
						meeting.getMeetingInfo(value);
						//checkSkipedAttendeeList = meeting.getAttendee(); 
						for (String attendId : meeting.getAttendee()) {
							checkSkipedAttendeeList.add(attendId);
						} 
						break;
					} else {						
						throw new EditMeetingException("edit-meeting : meeting-id ("+value+") not in db");
					}
				case "date" :
					if(checkDateValid(value)){
						meeting.setDate(value);  // changing current date
						timeChanged = true;
						break;
					} else {						
						throw new EditMeetingException("invalid date ("+value+") for edit-meeting command");
					}
				case "start-time" :					
					if(checkTimeValid(value)){
						meeting.setStartTime(value);  // changing start time
						timeChanged = true;
						break;
					} else {
						throw new EditMeetingException("invalid time("+value+") for edit-meeting command");
					}
				case "end-time" :
					if(checkTimeValid(value)){
						meeting.setEndTime(value);   // changing end time
						timeChanged = true;
						break;
					} else {
						throw new EditMeetingException("invalid time ("+value+") for edit-meeting command");
					}
				case "room-id" :
					if(checkRoomIdValid(value)){
						meeting.setRoomId(value);		// changing room id		
						roomChanged = true;
						break;
					} else {
						throw new EditMeetingException("invalid room id ("+value+") for edit-meeting command");
					}
				case "description" :					
					if(checkStrLenValid(value)){
						meeting.setDescription(value);		// changing description		
						break;
					} else {
						throw new EditMeetingException("description is too long for edit-meeting command");
					}
				case "attendee" :					
					if(checkEmpolyeeIdValid(value)) {
						String regex = ",|\\s+";
				        String[] attendees = value.split(regex);
				        for (String attendee : attendees) {
				        	/* if blank or null attendee contains, continues */
							if (CommonUtil.nullTrim(attendee).equals("")) continue;
							
							if (this.atteedeeOption != null) {
								// Only attendee add option
								if (this.atteedeeOption.equals(SysConfig.addTag)) {
									LinkedList<String> lis = meeting.getAttendee();
									for (int k = 0; k < lis.size(); k++) {
										String attendeeID = (String) lis.get(k);
										if (attendeeID.equalsIgnoreCase(attendee)) { // same
																					// person
																					// already
																					// exsits
											throw new EditMeetingException("empolyee id ("
															+ value
															+ ") already exists in the meeting attendees for edit-meeting command" );
										}
									}
									meeting.addAttendee(attendee);
									// Only attendee remove option
								} else if (this.atteedeeOption
										.equals(SysConfig.removeTag)) {
									LinkedList<String> lis = meeting.getAttendee();
									LinkedList<String> modified_lis = new LinkedList<String>();
									for (int k = 0; k < lis.size(); k++) {
										String attendeeID = (String) lis.get(k);
										if (!attendeeID.equalsIgnoreCase(attendee)) { // if
																					// different
											modified_lis.add(attendeeID);
										}
									}
									/* again modified list mapping to meeting */
									meeting.setAttendee(modified_lis);
								}
							}
							/* without attendee option, just add attendee */
							else {
								meeting.addAttendee(attendee);
							}
				        }
						attendeeChanged = true;
						break;
					}
					else {
						throw new EditMeetingException("invalid empolyee id ("+value+") for edit-meeting");
					}
				default :
					throw new EditMeetingException("invalid arguments : " + name + "for edit-meeting");
			}			
		}
		
		if(meeting.getMeetingId() == null){		
			throw new EditMeetingException("Missing meeting id for edit meeting command");
		}
			/* check room and employee schedule */
		ableToAttendWithoutConflict();			
			/* Databse writing */		
		if (!updateMeetingInfo(this.meeting)) {
				//System.out.println("edit-meeting : meeting ID("+meeting.getMeetingId()+") is failed");
			throw new EditMeetingException("editing meeting to database is failed for edit meeting command");
		}					
	}
	/**
	 * Check if new meeting date is available for another meeting date, company's holiday, and ateendee's schedules.
	 * @return
	 */
	public void ableToAttendWithoutConflict() throws EditMeetingException{
		
		// 1. first check room available
		if (timeChanged || roomChanged) {			
			Room room = new Room();
			room.setRoomID(meeting.getRoomId());
			try {
				room.roomAvailable(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
			} catch (TimeConflictException tce) {
				throw new EditMeetingException(tce.getMessage() + "for edit meeting command");
			}			
		}				
		// 2. check holiday
		AddHoliday holi = new AddHoliday();		
		try {
			holi.checkAvailableWithHoliday(meeting.getDate());			
		} catch (TimeConflictException tce) {
			throw new EditMeetingException(tce.getMessage() + "for edit meeting command");			
		}				
		// 3. check all attendees' meeting and vacation date
		if (timeChanged || attendeeChanged) {
			
			LinkedList<String> attendList = meeting.getAttendee();
			
			for (int i=0;i<attendList.size();i++) {				
				Employee emp = new Employee();
				emp.setEmployeeID((String) attendList.get(i));
				
				/*check meeting*/
				try {
					// only new attendees check					
					if (!checkSkipedAttendeeList.contains((String) attendList.get(i))) {
						emp.checkAvailableWithMeeting(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
					}
				} catch (TimeConflictException tce) {
					throw new EditMeetingException(tce.getMessage() + "for edit meeting command");
				}								
				/*check vacation*/
				try {
					// only new attendees check
					if (!checkSkipedAttendeeList.contains((String) attendList.get(i))) {
						emp.checkAvailableWithVacation(meeting.getDate());
					}
				} catch (TimeConflictException tce) {
					throw new EditMeetingException(tce.getMessage() + "for edit meeting command");
				}				
			}
		}		
		
	}
	/**
	 * Update the gathering new meeting information into the database
	 * @param minfo
	 * @return
	 */
	public boolean updateMeetingInfo(Meeting minfo) {
		
		boolean bSuccess = false;	
		
		String uptMeetQuery = "UPDATE TB_MEETING SET meetDATE = ?, startTIME = ?, endTIME = ?, "
							+ "description = ?, roomID = ? WHERE meetID = ?";
		
		String delAttendeeQuery = "DELETE FROM TB_ATTENDEE WHERE meetID = ? ";
		String insAttendeeQuery = "INSERT INTO TB_ATTENDEE(meetID, employeeID) VALUES (?,?) ";
		
		Sql db = new Sql();		
		db.setQuery(uptMeetQuery);		
		db.setParameter(1, minfo.getDate());
		db.setParameter(2, minfo.getStartTime());
		db.setParameter(3, minfo.getEndTime());
		db.setParameter(4, minfo.getDescription());
		db.setParameter(5, minfo.getRoomId());
		db.setParameter(6, minfo.getMeetingId());
		int n = db.write();
		/* if insMeetQuery is successful */
		if (n > 0) {
			/* First previous data delete!*/
			db.setQuery(delAttendeeQuery);
			db.setParameter(1, minfo.getMeetingId());
			n = db.write();
			/* New Data insert */
			LinkedList<String> lis = minfo.getAttendee();
			for (int i=0;i<lis.size();i++) {
				db.setQuery(insAttendeeQuery);	
				db.setParameter(1, minfo.getMeetingId());
				db.setParameter(2, (String) lis.get(i));	
			//	System.out.println(minfo.getMeetingId() + ":" + (String) lis.get(i));
				db.write();
			}
			bSuccess = true;
		}
		db.close();
		
		return bSuccess;		
	}
}
