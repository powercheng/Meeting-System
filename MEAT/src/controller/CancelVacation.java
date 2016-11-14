package controller;

import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;

public class CancelVacation extends Command {
	
	private Vacation vacation;	
	private JSONArray command_array;
	
	public CancelVacation(JSONArray command_array) {
		super();
		this.command_array = command_array;
		this.vacation = new Vacation();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for canceling vacation command");
			return;
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "employee-id" :
					vacation.setEmpolyeeId(value);
					break;					
				default :
					System.out.println("invalid arguments : " + name + "for cancel vaction");
					break;
			}			
		}
		
		if ( vacation.getEmpolyeeId() != null ) {
			if (!cancelVactionInfo(this.vacation)) {
				System.out.println("Cancel vacation (empID : "+vacation.getEmpolyeeId()+") failed");
			}
		}
		
		//viewprint();		
	}
	
	public boolean cancelVactionInfo(Vacation vinfo) {
		
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
