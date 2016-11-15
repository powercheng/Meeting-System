package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import View.Messageout;
import model.Employee;
import model.Meeting;
import model.Room;

public class Command {
	
	public String execute(){
		return "";		
	}
	
	
	public boolean checkTimeValid(String str){
		str = str.trim();
		
		if(str.length() == 4){
			int hour = Integer.parseInt(str.substring(0, 1));
			int min = Integer.parseInt(str.substring(2, 4));
			return hour >= 0 && hour < 10 && min >=0 && min <=60;
		} else if(str.length() == 5) {
			int hour = Integer.parseInt(str.substring(0, 2));
			int min = Integer.parseInt(str.substring(3, 5));
			return hour >= 0 && hour <= 24 && min >=0 && min <=60;
		} else {
			return false;
		}
	}		
      
	private static boolean isLeapYear(int y) {
		return y % 4 == 0 && (y % 400 == 0 || y % 100 != 0);  
	}	
	
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
	
	public boolean checkMeetingIdValid(String meetingID){
		/* if meetID (from DB) == null then no such meeting ID*/
		Meeting mt = new Meeting();
		mt.getMeetingInfo(meetingID);  // get and setting database information		
		if (mt.getMeetingId() == null) {
			return false;
		}
		return true;
	}
	
	
	public boolean checkEmpolyeeIdValid(String employeeID){
		/* if employeeID (from DB) == null then no such employeeID*/
		/* Just external attendee having only name not employeeID
		Employee emp = new Employee();
		emp.getPersonInfo(employeeID);  // get and setting database information		
		if (emp.getEmployeeID() == null) {
			return false;
		}
		*/
		return true;
	}
	
	public boolean checkRoomIdValid(String roomID) {
		Room rm = new Room();
		rm.getRoomInfo(roomID); // GET AND SETTING DATABASE INFORMATION
		/* if roomID (fromDB) is null then no such room */
		if (rm.getRoomID() == null) {
			return false;
		}		
		return true;
	}
	
	public boolean checkStrLenValid(String str) {
		return str.length() < 1024;
	}
	
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
	
	public void viewprint() {
		Messageout msg = new Messageout();
		msg.run();
	}
}
