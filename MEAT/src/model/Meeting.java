package model;

public class Meeting {
	private String meetingId;
	private String date;
	private String startTime;
	private String endTime;
	private String roomId;
	private String attendee;
	private String description;
	
	
	public Meeting() {
		super();
	}
	
	public void readFromSql() {
		Sql server = new Sql();
		server.read();
		
	}
	
	public void writeToSql(){
		Sql server = new Sql();
		server.write();
	}
	
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getAttendee() {
		return attendee;
	}
	public void setAttendee(String attendee) {
		this.attendee = attendee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
