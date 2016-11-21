package controller;

import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.CancelVacationException;

/**
 * Cancel employee's scheduled vacation
 * @author group7
 *
 */
public class CancelVacation extends Command {
	
	private Vacation vacation;	
	private JSONArray command_array;
	/**
	 * constructor for script running mode
	 * @param command_array
	 */
	public CancelVacation(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.vacation = new Vacation();
	}
	/**
	 * check parameters validity and deleting passing vacation information 
	 */
	@Override
	public void execute() throws CancelVacationException {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			throw new CancelVacationException("No argument for cancel-vacation command");
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
						throw new CancelVacationException("invalid empolyee id("+value+") for cancel-vacation command");
					}
				default :
					throw new CancelVacationException("invalid arguments : " + name + "for cancel-vacation");					
			}			
		}
		if ( vacation.getEmpolyeeId() == null ) {
			throw new CancelVacationException("Missing vacation id for cancel vacation command");
		}	
		if (!cancelVacationInfo(this.vacation)) {
			throw new CancelVacationException("cancel vacation to database is failed for cancel vacation command");
		}	
	}
	/**
	 * Delete the passing vacation information from database
	 * @param vinfo
	 * @return
	 */
	public boolean cancelVacationInfo(Vacation vinfo) {
		
		boolean bSuccess = false;
		
		String delVacQuery = "DELETE FROM TB_VACATION WHERE employeeID = ? ";
		
		Sql db = new Sql();		
		db.setQuery(delVacQuery);
		db.setParameter(1, vinfo.getEmpolyeeId());
		
		int n = db.write(); 
		
		if (n > 0) 
			bSuccess = true;
		
		db.close();
		
		return bSuccess;		
	}
}
