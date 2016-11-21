package controller;
import model.Employee;
import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.TimeConflictException;
import exceptions.AddVacationException;
/**
 * Scheduling class for employee's vacation
 * @author group7
 *
 */
public class AddVacation extends Command {
	
	private Vacation vacation;	
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public AddVacation(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.vacation = new Vacation();
	}
	/**
	 * analyzing passing data array and check validity, insert commands data into database
	 */
	@Override
	public void execute() throws AddVacationException {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new AddVacationException("No argument for add-holiday command");
		}
		for(int i = 0; i < command_array.size(); i++) {
			
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			
			switch(name) {
				case "employee-id" :
					if(checkEmpolyeeIdValid(value)){
						vacation.setEmpolyeeId(value);
						break;
					} else {
						throw new AddVacationException("invalid employee id("+value+") for add-vacation command");
					}
				case "start-date" :
					if(checkDateValid(value)){
						vacation.setStartDate(value);
						break;
					} else {
						throw new AddVacationException("invalid start-date("+value+") for add-vacation command");
					}
				case "end-date" :
					if(checkDateValid(value)){
						vacation.setEndDate(value);
						break;
					} else {
						throw new AddVacationException("invalid end-date("+value+") for add-vacation command");
					}
				default :
					throw new AddVacationException("invalid arguments : " + name + " for add-vacation");
			}			
		}
		
		if ( vacation.getEmpolyeeId() == null ) {
			throw new AddVacationException("missing empolyee id argument for addVacation command");
		}
		ableVacationWithoutConflict();
		if (!addVactionInfo(this.vacation)) {
			throw new AddVacationException("adding vacation to database is failed for adding vacation command");
		} 
		
		
	}
	/**
	 * Check if employee's vacation dates conflict with scheduled meeting date
	 * @return
	 */
	public void ableVacationWithoutConflict() throws AddVacationException{
		
		// Check if conflict with meeting schedules
		Employee emp = new Employee();
		emp.setEmployeeID(vacation.getEmpolyeeId());
		
		try { 
			emp.checkAvailableWithMeeting(vacation.getStartDate(), vacation.getEndDate());
		} catch (TimeConflictException tce) {
			throw new AddVacationException(tce.getMessage() + "for adding vacation command");
		}
	}
	/**
	 * add employee's vacation information
	 * @param vinfo
	 * @return
	 */
	public boolean addVactionInfo(Vacation vinfo) {
		
		boolean bSuccess = false;
		
		String insVacQuery = "INSERT INTO TB_VACATION(employeeID, startDATE, endDATE) VALUES (?,?,?) ";
		
		Sql db = new Sql();		
		db.setQuery(insVacQuery);
		db.setParameter(1, vinfo.getEmpolyeeId());
		db.setParameter(2, vinfo.getStartDate());
		db.setParameter(3, vinfo.getEndDate());
		int n = db.write(); 
		
		if (n > 0) 
			bSuccess = true;
		
		db.close();
		
		return bSuccess;		
	}
}
