package controller;

import model.Meeting;
import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;

public class AddVacation extends Command {
	
	private Vacation vacation;	
	private JSONArray command_array;
	
	public AddVacation(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.vacation = new Vacation();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for adding vacation command");
			return;
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
						System.out.println("invalid employee id("+value+") for adding vacation command");
						return;
					}
				case "start-date" :
					if(checkDateValid(value)){
						vacation.setStartDate(value);
						break;
					} else {
						System.out.println("invalid start-date("+value+") for adding vacation command");
						return;
					}
				case "end-date" :
					if(checkDateValid(value)){
						vacation.setEndDate(value);
						break;
					} else {
						System.out.println("invalid end-date("+value+") for adding vacation command");
						return;
					}
				default :
					System.out.println("invalid arguments : " + name + "for adding vaction");
					break;
			}			
		}
		
		if ( vacation.getEmpolyeeId() != null ) {
			if (!addVactionInfo(this.vacation)) {
				System.out.println("Add vacation (empID : "+vacation.getEmpolyeeId()+") failed");
			}
		}
		
		//viewprint();		
	}
	
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
