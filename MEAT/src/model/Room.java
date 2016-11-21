package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.TimeConflictException;
import controller.Command;
/**
 * Room model class
 * @author group7
 *
 */
public class Room {
		
	private String roomID;
	private String building;
	private String floor;
	private String occupancy;
	/**
	 * default constructor
	 */
	public Room() {
		super();
		// TODO Auto-generated constructor stub
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
	/**
	 * Get room information from database and set the fetched data into this class
	 * @param id
	 */
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
	
	/**
	 * Check if target room is opened between specific time range 
	 * @param meetDate  (MMDDYYYY)
	 * @param fromTime  (HH24:MI)
	 * @param endTime   (HH24:MI)
	 * @throws TimeConflictException
	 */
	public void roomAvailable(String meetDate, String fromTime, String endTime) throws TimeConflictException {
		
		Command cmd = new Command();
		
		if (getRoomID() == null) {
			throw new TimeConflictException("Room ID is not set");
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
								+ "  FROM TB_MEETING WHERE meetDATE = ? AND roomID = ? "
								+ " )"
								+ " WHERE time(startTIME) between time(?) and time(?) or "
								+ " 	  time(endTIME)   between time(?) and time(?) or "
								+ " 	  time(?)  between time(startTIME) and time(endTIME) "; //endTIME
								/* basically we face problem if we use 1-12 time system without AM or PM*/
			db.setQuery(infoQuery);
			// fromTIME 1:53 -> 01:53 transform
			if (fromTime.length() == 4) fromTime = "0" + fromTime;
			if (endTime.length()  == 4) endTime  = "0" + endTime;
			
			db.setParameter(1, meetDate);
			db.setParameter(2, getRoomID());
			db.setParameter(3, fromTime);
			db.setParameter(4, endTime);
			db.setParameter(5, fromTime);
			db.setParameter(6, endTime);
			db.setParameter(7, endTime);
						
			JSONArray checkArr = db.read();	
			
			db.close();
			
			if (checkArr.size() > 0) {
				// already scheduled
				throw new TimeConflictException("Room ID("+getRoomID()+") already has a meeting at the same time."); 
			}
				
		} else {
			throw new TimeConflictException("Date and Time are not valid");	
		}		
	}	
}
