package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Employee {
	
	private Meeting[] meeting;
	private Vacation[] vacation;
	
	private String employeeID;
	private String firstName;
	private String lastName;
	private String email;
	private String title;
	private int totalVACATIONDAY;
	
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

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotalVACATIONDAY() {
		return totalVACATIONDAY;
	}

	public void setTotalVACATIONDAY(int totalVACATIONDAY) {
		this.totalVACATIONDAY = totalVACATIONDAY;
	}
	
	public void getPersonInfo(String empID) {
		
		if (empID != null && empID.length() > 0) {
			
			Sql db = new Sql();
			String infoQuery = "SELECT employeeID, firstNAME, lastNAME, email, title, totalVACATIONDAY"
					+ " FROM TB_EMPLOYEE WHERE employeeID = ? ";
			db.setQuery(infoQuery);
			db.setParameter(1, empID);
			JSONArray recArr = db.read();		
			for (int i=0; i<recArr.size(); i++) {
				// In reality, every time just one record
				JSONObject rsObj = (JSONObject) recArr.get(i);
				/* Set personal information */
				setEmployeeID((String) rsObj.get("employeeID"));
				setFirstName((String) rsObj.get("firstNAME"));
				setLastName((String) rsObj.get("lastNAME"));
				setEmail((String) rsObj.get("email"));
				setTitle((String) rsObj.get("title"));
				setTotalVACATIONDAY(Integer.parseInt((String) rsObj.get("totalVACATIONDAY")));
			}	
			
			if (recArr.size() == 0) {
				System.out.println("No such employee");
			}
		} else {
			System.out.println("employeeID is not passed");
		}
	}
	
}
