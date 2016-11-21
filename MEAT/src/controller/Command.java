package controller;

import common.CommonUtil;
import model.Employee;
import model.Meeting;
import model.Room;
/**
 * super class for respective action handling class
 * @author group7
 *
 */
public class Command {
	
	/**
	 * To allow override method of child classes to handle actions
	 * @return
	 * @throws Exception 
	 */
	public void execute() throws Exception{
				
	}
	
	/**
	 * Check if time-format is valid (HH24:MI) 
	 * @param str
	 * @return
	 */
	public boolean checkTimeValid(String str){
		str = str.trim();
		
		if(str.length() == 4){
			int hour = Integer.parseInt(str.substring(0, 1));
			int min = Integer.parseInt(str.substring(2, 4));
			return hour >= 0 && hour < 10 && min >=0 && min <=60;
		} else if(str.length() == 5) {
			int hour = Integer.parseInt(str.substring(0, 2));
			int min = Integer.parseInt(str.substring(3, 5));
			return hour >= 0 && hour < 24 && min >=0 && min <=60;
		} else {
			return false;
		}
	}		
    /**
     * To check whether leap year or not 
     * @param y
     * @return
     */
	private static boolean isLeapYear(int y) {
		return y % 4 == 0 && (y % 400 == 0 || y % 100 != 0);  
	}	
	/**
	 * Check date format validity (MMDDYYYY)
	 * @param str
	 * @return
	 */
	public boolean checkDateValid(String str){
		str = str.trim();
		if(str.length() == 8) {
			int d = Integer.parseInt(str.substring(2, 4));
			int m = Integer.parseInt(str.substring(0, 2));
			int y = Integer.parseInt(str.substring(4, 8));

			if (d < 1 || m < 1 || m > 12) {
				return false; 
			}
   
			if (m == 2) { 
				if (isLeapYear(y)) {
					return d <= 29; 
				}
				else {
					return d <= 28; 				
				}
			} 
			else if (m == 4 || m == 6 || m == 9 || m == 11){
				return d <= 30;
			}				 
			else  {
				return d <= 31; 
			}
		} else {
			return false;
		}
	}
	/**
	 * Verify passing meeting ID is in the database.
	 * @param meetingID
	 * @return
	 */
	public boolean checkMeetingIdValid(String meetingID){
		/* if meetID (from DB) == null then no such meeting ID*/
		Meeting mt = new Meeting();
		mt.getMeetingInfo(meetingID);  // get and setting database information		
		if (mt.getMeetingId() == null) {
			return false;
		}
		return true;
	}	
	/**
	 * Check if employeeID is in the database.
	 * @param employeeID
	 * @return
	 */
	public boolean checkEmpolyeeIdValid(String employeeID){
		/* if employeeID (from DB) == null then no such employeeID*/	
		String regex = ",|\\s+";
        String[] attendees = employeeID.split(regex);
        for(String attendee : attendees){
			/* if blank or null attendee contains, continues */
			if (CommonUtil.nullTrim(attendee).equals("")) 
				continue;
			
			Employee emp = new Employee();
			emp.getPersonInfo(attendee);  // get and setting database information		
			if (emp.getEmployeeID() == null) {
				System.out.println("invalid empolyee id ("+attendee+") for adding meeting command");
				return false;
			}
		}
		return true;
	}
	/**
	 * Check if roomID is in the database
	 * @param roomID
	 * @return
	 */
	public boolean checkRoomIdValid(String roomID) {
		Room rm = new Room();
		rm.getRoomInfo(roomID); // GET AND SETTING DATABASE INFORMATION
		/* if roomID (fromDB) is null then no such room */
		if (rm.getRoomID() == null) {
			return false;
		}		
		return true;
	}
	/**
	 * Check passing string is less than 1024 bytes
	 * @param str
	 * @return
	 */
	public boolean checkStrLenValid(String str) {
		return str.length() < 1024;
	}
	/**
	 * Check if time1 (str1) precedes time2 (str2)
	 * @param str1
	 * @param str2
	 * @return
	 */
	public boolean checkTimeConflict(String str1,String str2) {
		int hour1 = Integer.parseInt(str1.substring(0, str1.length()-3));
		int hour2 = Integer.parseInt(str2.substring(0, str2.length()-3));
		int min1 = Integer.parseInt(str1.substring(str1.length()-2, str1.length()));
		int min2 = Integer.parseInt(str2.substring(str2.length()-2, str2.length()));
		
		if(hour2 > hour1){		
			return true;
		} else if(hour1 == hour2){
			if(min2 > min1){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}	
	
}
