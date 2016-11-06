package model;

public class Vacation {
	private String vacationId;
	private String empolyeeId;
	private String startDate;
	private String endDate;
	private String description;		
	
	public Vacation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void readFromSql() {
		Sql server = new Sql();
		server.read();
		
	}
	
	public void writeToSql(){
		Sql server = new Sql();
		server.write();
	}
	
	public String getEmpolyeeId() {
		return empolyeeId;
	}

	public void setEmpolyeeId(String empolyeeId) {
		this.empolyeeId = empolyeeId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVacationId() {
		return vacationId;
	}

	public void setVacationId(String vacationId) {
		this.vacationId = vacationId;
	}
	
	
}
