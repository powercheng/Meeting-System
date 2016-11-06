package model;

public class Employee {
	private Meeting[] meeting;
	private Vacation[] vacation;
	
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Meeting[] getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting[] meeting) {
		this.meeting = meeting;
	}
	public Vacation[] getVacation() {
		return vacation;
	}
	public void setVacation(Vacation[] vacation) {
		this.vacation = vacation;
	}
	
	
}
