package controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.CommonUtil;

public class CommandFactory {

	public CommandFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

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
						default :
							System.out.println("invalid command : " + name);
					}
					if(command != null) {
						command.execute();
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

