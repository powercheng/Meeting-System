package controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.CommonUtil;
import common.SysConfig;
/**
 * This class is used for various script commands using factory pattern
 * @author group7
 */
public class CommandFactory {
	/**
	 * default constructor
	 */
	public CommandFactory() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * analyzing passing commands and initialize the suitable class for the commands
	 * and finally execute the command
	 * @param jsonData
	 */
	public void run(String jsonData) {
		// TODO Auto-generated method stub
		if (CommonUtil.nullTrim(jsonData).length()> 0) {
			JSONParser parser = new JSONParser();
			
			try {							
				JSONArray jsonArray = (JSONArray) parser.parse(jsonData);					
				JSONObject jsonObj = (JSONObject) jsonArray.get(0);
				JSONArray commands_array = (JSONArray) jsonObj.get("commands");
				
				for(int i = 0; i < commands_array.size(); i++) {
					JSONObject command_json = (JSONObject) commands_array.get(i);
					String name = (String) command_json.get("name");
					JSONArray command_array = (JSONArray) command_json.get("arguments");
					//System.out.println(name);
					Command command = null;
					switch(name) {
						case "add-meeting" :
							command = new AddMeeting(command_array);							
							break;
						case "edit-meeting-details" :
							command = new EditMeeting(command_array);							
							break;
						case "edit-meeting-add-attendees":
							command = new EditMeeting(command_array, SysConfig.addTag); // Attendee option = add	
							break;
						case "edit-meeting-remove-attendees" :
							command = new EditMeeting(command_array, SysConfig.removeTag); // Attendee option = remove							
							break;
						case "delete-meeting" :
							command = new CancelMeeting(command_array);							
							break;
							
						case "add-vacation" :
							command = new AddVacation(command_array);							
							break;
							
						case "delete-vacation" :
							command = new CancelVacation(command_array);							
							break;
							
						case "add-holiday" :
							command = new AddHoliday(command_array);							
							break;
						
						case "print-schedule-all" :
							command = new PrintScheduleAll(command_array);							
							break;
						
						case "print-schedule-room" :
							command = new PrintScheduleRoom(command_array);							
							break;
						
						case "print-schedule-employee" :
							command = new PrintScheduleEmployee(command_array);							
							break;
						
						default :
							System.out.println(name + " : invalid command");
					}
					/* execute */
					if(command != null) {
						String result = command.execute();
						System.out.println("### " + name + " : " + result);
					}					
				}
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			catch (ClassCastException e) {
				e.printStackTrace();
			}finally {
				
			}
		}
	}
}
