package controller;

import model.Meeting;
import model.Room;
import model.Sql;
import model.Vacation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import View.Messageout;
import common.SysConfig;

public class PrintScheduleRoom extends Command {
	private Room room;
	private String srchStartDay;
	private String srchEndDay;
	private String outfileName;
	
	
	private JSONArray command_array;
	
	public PrintScheduleRoom(JSONArray command_array) {
		super();
		this.command_array = command_array;		
		this.room = new Room();
	}
	
	@Override
	public String execute() {		
		// TODO Auto-generated method stub	
		if(command_array == null || command_array.isEmpty()) {
			System.out.println("No argumets for print-schedule-room");
			return SysConfig.fail;
		}
		for(int i = 0; i < command_array.size(); i++) {
			JSONObject command_json = (JSONObject) command_array.get(i);
			String name = (String) command_json.get("name");
			String value = (String) command_json.get("value");
			switch(name) {
				case "room-id" :
					room.getRoomInfo(value);
					break;	
				case "start-date" :
					if(checkDateValid(value)){
						setSrchStartDay(value);
						break;
					} else {
						return SysConfig.fail;
					}
				case "end-date" :
					if(checkDateValid(value)){
						setSrchEndDay(value);
						break;
					} else {
						return SysConfig.fail;
					}
				case "output-file" :
					setOutfileName(value);
					break;
					
				default :
					System.out.println("invalid arguments : " + name + "for print-schedule-room");
					return SysConfig.fail;
			}			
		}
		
		if (!checkCondition()) {
			return SysConfig.fail;
		}
		
		
		return SysConfig.success;
		
	}
	
	public JSONObject getSearchResult() {
		Sql db = new Sql();
		String meetDetailQuery = "SELECT meetID as meeting-id, meetDATE as date, startTIME as start-time, endTime as end-time, "
				+ "roomID AS room-id, description FROM TB_MEETING WHERE roomID = ? ";
	}
	
	public boolean printOutFile() {
		
		
		return true;
	}
	
	public boolean checkCondition() {
		
		/* Necessary information check */
		if (room.getRoomID() == null) {
			System.out.println("No roomID for print-schedule-room");
			return false;
		}
		if (getSrchStartDay() == null) {
			System.out.println("No start-date for print-schedule-room");
			return false;
		}
		if (getSrchEndDay() == null) {
			System.out.println("No end-date for print-schedule-room");
			return false;
		}
		if (getOutfileName() == null) {
			System.out.println("No output-file for print-schedule-room");
			v
		}		
		
		return true;
	}
	
	public String getSrchStartDay() {
		return srchStartDay;
	}

	public void setSrchStartDay(String srchStartDay) {
		this.srchStartDay = srchStartDay;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public String getSrchEndDay() {
		return srchEndDay;
	}

	public void setSrchEndDay(String srchEndDay) {
		this.srchEndDay = srchEndDay;
	}

	public String getOutfileName() {
		return outfileName;
	}

	public void setOutfileName(String outfileName) {
		this.outfileName = outfileName;
	}

}
