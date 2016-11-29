package model;

/**
 * Vacation model class
 * @author group7
 *
 */
public class Vacation {
	
	private String empolyeeId;
	private String startDate;
	private String endDate;
	/**
	 * default constructor
	 */
	public Vacation() {
		super();
		// TODO Auto-generated constructor stub
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
	/**
	 * get an employee's vacation list
	 * @param empID
	 * @return
	 */
/*	
	public JSONArray getVacationList(String empID) {
		
		Sql db = new Sql();
		String infoQuery = "SELECT TA.employeeID, TB.firstNAME||' '||TB.lastNAME as name, TA.startDATE, TA.endDATE "
							+ " FROM TB_VACATION TA INNER JOIN TB_EMPLOYEE TB ON TA.employeeID = TB.employeeID "
					 	    + "  WHERE TA.employeeID = ?   " ;
		db.setQuery(infoQuery);		
		db.setParameter(1, empID);
		JSONArray vacArr = db.read();				
		db.close();		
		return vacArr;
		
	}
*/
	/**
	 * Print an employee's vacation schedule on the command screen
	 * @param empID
	 */
/*	
	public void printScreenVacationList(String empID) {
		
		JSONArray vacArr = getVacationList(empID);
		System.out.println("### Scheduled vaction ### ");
		System.out.println("# EmloyeeID(NAME)                     #STARTDAY       #ENDDAY   ");
    	System.out.println("-------------------------------------------------------------------");
    	for (int i=0;i<vacArr.size();i++) {
    		JSONObject jsObj = (JSONObject) vacArr.get(i);
    		String empName = empID + "("+(String) jsObj.get("name")+")";
    		empName = CommonUtil.blankPadding(empName, 30);
    		
    		String startDay = (String) jsObj.get("startDATE");
    		String endDay   = (String) jsObj.get("endDATE");
    		System.out.println("# " + empName + "      " + CommonUtil.dateFormat(startDay,"MMddyyyy","MM.dd.yyyy") +
    		"      " + CommonUtil.dateFormat(endDay,"MMddyyyy","MM.dd.yyyy"));     		
    	}
    	System.out.println("-------------------------------------------------------------------");	
	}
*/	
}
