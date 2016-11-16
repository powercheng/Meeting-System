package controller;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import common.SysConfig;
import common.TimeConflictException;
import model.Meeting;
import model.Sql;
import model.Vacation;
/**
 * This class is used for add company's holiday into database
 * @author group7
 */
public class AddHoliday extends Command  {
	
	private JSONArray command_array;
	private String startDate;
	private String endDate;
	private String description;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public AddHoliday(JSONArray command_array) {
		super();
		this.command_array = command_array;
	}
	/**
	 * constructor for interactive mode
	 */
	public AddHoliday() {
		super();
	}
	/**
	 * analyze commands and put analyzed data into database 
	 */
	@Override
	public String execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argument for add-holiday command");
			return SysConfig.fail;
		}
		for(int i = 0; i < command_array.size(); i++) {
			
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			
			switch(name) {
				case "start-date" :
					if(checkDateValid(value)){
						setStartDate(value);
						break;
					} else {
						System.out.println("invalid start-date("+value+") for add-holiday command");
						return SysConfig.fail;
					}
				case "end-date" :
					if(checkDateValid(value)){
						setEndDate(value);
						break;
					} else {
						System.out.println("invalid end-date("+value+") for add-holiday command");
						return SysConfig.fail;
					}
				case "description" :
					if(checkStrLenValid(value)){
						setDescription(value);
						break;
					} else {
						System.out.println("description is too long for add-holiday command");
						return SysConfig.fail;
					}
				default :
					System.out.println("invalid arguments : " + name + "for add-holiday");
					return SysConfig.fail;
			}			
		}
		
		if(checkHolidyArgument()) {	
			
			if (!insertHolidayInfo()) {			
				return SysConfig.fail;
			} else {
				return SysConfig.success;
			}		
			
		} else {
			return SysConfig.fail;
		}
	}
	/**
	 * Check variables' validity
	 * @return
	 */
	private boolean checkHolidyArgument() {
		
		if (getStartDate() == null || getEndDate() == null || getDescription() == null) {
			System.out.println("Missing argumets for adding holiday command");
			return false;
		}
		return true;
	}
 
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * insert the final holiday information into database
	 * @return
	 */
	public boolean insertHolidayInfo() {
		
		boolean bSuccess = false;	
		
		String insHolidayQuery = "INSERT INTO TB_HOLIDAY(startDATE, endDATE, description) "
							+ " VALUES (?, ?, ?)";
				
		Sql db = new Sql();		
		db.setQuery(insHolidayQuery);
		db.setParameter(1, getStartDate());
		db.setParameter(2, getEndDate());
		db.setParameter(3, getDescription());
		int n = db.write();
		/* if insHolidayQuery is successful */
		if (n > 0) bSuccess = true;
		
		db.close();
		
		return bSuccess;		
	}
	/**
	 * Check if meeting data conflicts with stored company's holiday 
	 * @param meetDate
	 * @throws TimeConflictException
	 */
	public void checkAvailableWithHoliday(String meetDate) throws TimeConflictException {
		
		Command cmd = new Command();
	
		if (cmd.checkDateValid(meetDate)) 
		{			
			Sql db = new Sql();
			/* MEET DAY BETWEEN HOLIDAY CAUSE CONFLICT */
			String infoQuery = "SELECT startDATE, endDATE, description FROM TB_HOLIDAY "
								+ "  WHERE "
								+ "  ? between  substr(startDATE,5,4)||substr(startDATE,0,3)||substr(startDATE,3,2) "
								+ "  	   and  substr(endDATE,5,4)||substr(endDATE,0,3)||substr(endDATE,3,2) ";
			db.setQuery(infoQuery);
			/* DATE format modify MMddyyyy - > yyyyMMdd */
			meetDate = CommonUtil.dateFormat(meetDate, "MMddyyyy", "yyyyMMdd");			
			db.setParameter(1, meetDate);		
						
			JSONArray checkArr = db.read();	
			
			db.close();
		//	System.out.println(checkArr.size());
			if (checkArr.size() > 0) {
				throw new TimeConflictException("Meeting day("+meetDate+") is in the company holiday season."); 
			}
			
		} else {
			throw new TimeConflictException("MeetDate is not valid");
		}			
	}
}
