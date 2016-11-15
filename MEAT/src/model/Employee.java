package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import common.SysConfig;
import controller.Command;

public class Employee {
	
	private String employeeID;
	private String firstName;
	private String lastName;
	private String email;
	private String title;
	private int totalVACATIONDAY;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotalVACATIONDAY() {
		return totalVACATIONDAY;
	}

	public void setTotalVACATIONDAY(int totalVACATIONDAY) {
		this.totalVACATIONDAY = totalVACATIONDAY;
	}
	/**
	 * Get person information correspond with employeeID
	 * @param empID
	 */
	public void getPersonInfo(String empID) {
		
		if (empID != null && empID.length() > 0) {
			
			Sql db = new Sql();
			String infoQuery = "SELECT employeeID, firstNAME, lastNAME, email, title, totalVACATIONDAY"
					+ " FROM TB_EMPLOYEE WHERE employeeID = ? ";
			db.setQuery(infoQuery);
			db.setParameter(1, empID);
			JSONArray recArr = db.read();		
			for (int i=0; i<recArr.size(); i++) {
				// In reality, every time just one record
				JSONObject rsObj = (JSONObject) recArr.get(i);
				/* Set personal information */
				setEmployeeID((String) rsObj.get("employeeID"));
				setFirstName((String) rsObj.get("firstNAME"));
				setLastName((String) rsObj.get("lastNAME"));
				setEmail((String) rsObj.get("email"));
				setTitle((String) rsObj.get("title"));
				setTotalVACATIONDAY(Integer.parseInt((String) rsObj.get("totalVACATIONDAY")));
			}	
			
			if (recArr.size() == 0) {
				System.out.println("No such employee");
			}
			
			db.close();
			
		} else {
			System.out.println("employeeID is not passed");
		}
	}
	
	/**
	 * Check if employee conflicts with scheduled meeting for another meeting time 
	 * @param meetDate  (MMDDYYYY)
	 * @param fromTime  (HH24:MI)
	 * @param endTime   (HH24:MI)
	 * @return
	 */	
	public boolean checkAvailableWithMeeting(String meetDate, String fromTime, String endTime)  {
		
		Command cmd = new Command();
		
		if (getEmployeeID() == null) {
			System.out.println("Employee ID is not set");
			return false;
		}
		
		if (cmd.checkDateValid(meetDate) && fromTime != null && endTime != null) 
		{			
			Sql db = new Sql();
			String infoQuery = "SELECT roomID, startTIME, endTIME FROM ("
								+ " SELECT roomID,  "
								+ "  case when length(startTIME) = 4 then '0'||startTime "
								+ "  	  else startTIME end as startTIME, "
								+ "  case when length(endTime) = 4 then '0'||endTime "
								+ "       else endTime end as endTime "
								+ "  FROM TB_MEETING TA WHERE meetDATE = ? and exists "
								+ "   (SELECT 'X' FROM TB_ATTENDEE WHERE TA.meetID = meetID and employeeID = ?)"
								+ " )"
								+ " WHERE time(startTIME) between time(?) and time(?) or "
								+ " 	  time(endTIME)   between time(?) and time(?) or "
								+ " 	  time(?)  between time(startTIME) and time(endTIME) "; //endTIME
								/* basically we face problem if we use 1-12 time system without AM or PM*/
			db.setQuery(infoQuery);
			
			if (fromTime.length() == 4) fromTime = "0" + fromTime;
			if (endTime.length()  == 4) endTime  = "0" + endTime;
			
			db.setParameter(1, meetDate);
			db.setParameter(2, getEmployeeID());
			db.setParameter(3, fromTime);
			db.setParameter(4, endTime);
			db.setParameter(5, fromTime);
			db.setParameter(6, endTime);
			db.setParameter(7, endTime);
						
			JSONArray checkArr = db.read();	
			
			db.close();
		//	System.out.println(checkArr.size());
			if (checkArr.size() > 0) return false;  // already scheduled (conflicted)
				
		} else {
			System.out.println("Date and Time are not valid");	
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if employee conflicts with scheduled meeting for new vacation time
	 * @param vacationStartDate  (MMDDYYYY)
	 * @param vacationEndDate    (MMDDYYYY)
	 * @return 
	 */	
	public boolean checkAvailableWithMeeting(String vacationStartDate, String vacationEndDate)  {
		
		Command cmd = new Command();
		
		if (getEmployeeID() == null) {
			System.out.println("Employee ID is not set");
			return false;
		}
		
		if (cmd.checkDateValid(vacationStartDate) && cmd.checkDateValid(vacationEndDate)) 
		{			
			Sql db = new Sql();
			String infoQuery = "SELECT meetID FROM TB_MEETING TA"
								+ " WHERE substr(meetDATE,5,4)||substr(meetDATE,0,3)||substr(meetDATE,3,2)  "
								+ "  	  between ? and ? "
								+ "  	  and exists "
								+ "( SELECT 'X' FROM TB_ATTENDEE WHERE TA.meetID = meetID and employeeID = ? ) ";
								
								
			db.setQuery(infoQuery);
			
			/* DATE format modify MMddyyyy - > yyyyMMdd */
			vacationStartDate = CommonUtil.dateFormat(vacationStartDate, "MMddyyyy", "yyyyMMdd");
			vacationEndDate   = CommonUtil.dateFormat(vacationEndDate,   "MMddyyyy", "yyyyMMdd");
			
			db.setParameter(1, vacationStartDate);
			db.setParameter(2, vacationEndDate);
			db.setParameter(3, getEmployeeID());			
						
			JSONArray checkArr = db.read();	
			
			db.close();
		//	System.out.println(checkArr.size());
			if (checkArr.size() > 0) return false;  // already scheduled (conflicted)
				
		} else {
			System.out.println("Date and Time are not valid");	
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if employee conflicts with scheduled vacation for new meeting date 
	 * @param meetDate  (MMDDYYYY)
	 * @return
	 */	
	public boolean checkAvailableWithVacation(String meetDate)  {
		
		Command cmd = new Command();
		
		if (getEmployeeID() == null) {
			System.out.println("Employee ID is not set");
			return false;
		}
		
		if (cmd.checkDateValid(meetDate)) 
		{			
			Sql db = new Sql();
			String infoQuery = "SELECT startDATE, endDATE FROM TB_VACATION "
								+ "  WHERE employeeID = ? and   "
								+ "  ? between  substr(startDATE,5,4)||substr(startDATE,0,3)||substr(startDATE,3,2) "
								+ "  	   and  substr(endDATE,5,4)||substr(endDATE,0,3)||substr(endDATE,3,2) ";
			db.setQuery(infoQuery);
			/* DATE format modify MMddyyyy - > yyyyMMdd */
			meetDate = CommonUtil.dateFormat(meetDate, "MMddyyyy", "yyyyMMdd");
			db.setParameter(1, getEmployeeID());
			db.setParameter(2, meetDate);		
						
			JSONArray checkArr = db.read();	
			
			db.close();
		//	System.out.println(checkArr.size());
			if (checkArr.size() > 0) return false;  // already scheduled (conflicted)
				
		} else {
			System.out.println("Date is not valid");	
			return false;
		}		
		return true;
	}
	
}
