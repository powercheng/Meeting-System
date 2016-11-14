package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Room {
	
	private Meeting[] meeting;
	
	private String roomID;
	private String building;
	private String floor;
	private String occupancy;

	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Meeting[] getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting[] meeting) {
		this.meeting = meeting;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(String occupancy) {
		this.occupancy = occupancy;
	}
	
	public void getRoomInfo(String id) {
		
		if (id != null && id.length() > 0) {
			
			Sql db = new Sql();
			String infoQuery = "SELECT roomID, building, floor, occupancy"
									+ " FROM TB_ROOM WHERE roomID = ? ";
			db.setQuery(infoQuery);
			db.setParameter(1, id);
			JSONArray recArr = db.read();		
			for (int i=0; i<recArr.size(); i++) {
				// In reality, every time just one record
				JSONObject rsObj = (JSONObject) recArr.get(i);
				/* Set personal information */
				setRoomID((String) rsObj.get("roomID"));
				setBuilding((String) rsObj.get("building"));
				setFloor((String) rsObj.get("floor"));
				setOccupancy((String) rsObj.get("occupancy"));				
			}				
			if (recArr.size() == 0) {
				System.out.println("No such room");
			}			
			db.close();
			
		} else {
			System.out.println("roomID is not passed");
		}
	}
	
}
