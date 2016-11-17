package controller;

import model.Employee;
import model.Meeting;
import model.Room;
import model.Sql;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.SysConfig;
import common.TimeConflictException;
/**
 * Edit and change the scheduled meeting information
 * @author zoasw
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
	}
	/**
	 * Check and verify passing commands data, and update old one into new information
	 */
	@Override
	public String execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No arguments for edit-meeting command");
			return SysConfig.fail;
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
						checkSkipedAttendeeList = meeting.getAttendee(); // current list no need to check schedule;
						break;
					} else {						
						System.out.println("edit-meeting : meeting-id ("+value+") not in db");
						return SysConfig.fail;
					}
				case "date" :
					if(checkDateValid(value)){
						meeting.setDate(value);  // changing current date
						timeChanged = true;
						break;
					} else {						
						System.out.println("invalid date ("+value+") for edit-meeting command");
						return SysConfig.fail;
					}
				case "start-time" :					
					if(checkTimeValid(value)){
						meeting.setStartTime(value);  // changing start time
						timeChanged = true;
						break;
					} else {
						System.out.println("invalid time("+value+") for edit-meeting command");
						return SysConfig.fail;
					}
				case "end-time" :
					if(checkTimeValid(value)){
						meeting.setEndTime(value);   // changing end time
						timeChanged = true;
						break;
					} else {
						System.out.println("invalid time ("+value+") for edit-meeting command");
						return SysConfig.fail;
					}
				case "room-id" :
					if(checkRoomIdValid(value)){
						meeting.setRoomId(value);		// changing room id		
						roomChanged = true;
						break;
					} else {
						System.out.println("invalid room id ("+value+") for edit-meeting command");
						return SysConfig.fail;
					}
				case "description" :					
					if(checkStrLenValid(value)){
						meeting.setDescription(value);		// chaning description		
						break;
					} else {
						System.out.println("description is too long for edit-meeting command");
						return SysConfig.fail;
					}
				case "attendee" :					
					if(checkEmpolyeeIdValid(value)) {
						String regex = ",|，|\\s+";
				        String[] attendees = value.split(regex);
				        for (String attendee : attendees) {

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
											System.out
													.println("empolyee id ("
															+ value
															+ ") already exists in the meeting attendees");
											return SysConfig.fail;
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
						System.out.println("invalid empolyee id ("+value+") for edit-meeting");
						return SysConfig.fail;
					}
				default :
					System.out.println("invalid arguments : " + name + "for edit-meeting");
					return SysConfig.fail;
			}			
		}
		
		if(meeting.getMeetingId() != null){		
			
			/* check room and employee schedule */
			if (!ableToAttendWithoutConflict()) {
				return SysConfig.fail;
			}
			
			/* Databse writing */		
			if (!updateMeetingInfo(this.meeting)) {
				//System.out.println("edit-meeting : meeting ID("+meeting.getMeetingId()+") is failed");
				return SysConfig.fail;
			}			
			return SysConfig.success;
			
		} else {			
			return SysConfig.fail;
		}					
	}
	/**
	 * Check if new meeting date is available for another meeting date, company's holiday, and ateendee's schedules.
	 * @return
	 */
	public boolean ableToAttendWithoutConflict() {
		
		// 1. first check room available
		if (timeChanged || roomChanged) {			
			Room room = new Room();
			room.setRoomID(meeting.getRoomId());
			try {
				room.roomAvailable(meeting.getDate(), meeting.getStartTime(), meeting.getEndTime());
			} catch (TimeConflictException tce) {
				tce.printStackTrace();
				return false;
			}			
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
					tce.printStackTrace();
					return false;
				}								
				/*check vacation*/
				try {
					// only new attendees check
					if (!checkSkipedAttendeeList.contains((String) attendList.get(i))) {
						emp.checkAvailableWithVacation(meeting.getDate());
					}
				} catch (TimeConflictException tce) {
					tce.printStackTrace();
					return false;
				}				
			}
		}		
		return true;
		
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
