package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import common.CommonUtil;
import common.SysConfig;
import controller.AddHoliday;
import controller.AddMeeting;
import controller.AddVacation;
import controller.CancelMeeting;
import controller.CancelVacation;
import controller.Command;
import controller.CommandFactory;
import controller.EditMeeting;
import controller.PrintScheduleAll;
import controller.PrintScheduleEmployee;
import controller.PrintScheduleRoom;
import model.Employee;
import model.Meeting;
import model.Room;
import model.Vacation;
/**
 * Main controller in our MEAT system
 * @author group7
 */
public class Main {	
	/**
     * MEAT System begins !
     * 1. "No args" is interactive mode
     * 2. "args = scriptname" is script running mode
     * 3. "args = jsonDBfile" is to load external db into local db
     * @param args 
     */
    public static void main(String[] args) {
    	
	    System.out.println("************* Welcome to the MEAT ! ****************");
	        
	    Main m = new Main();
	    if (args.length == 0) {
	    	// no passing parameter means interactive mode
	    	m.printMainMenu();
	    } else {
	    	switch (args[0].toUpperCase()) {
	    		// External DB (employee.json and room.json in resource dir) loaded into local database
		    	case "DBSYNC":   
					/* TO DO */
		    		System.out.println("\n----- External DB syncronization is starting..-----\n");
		    		ExternalDBImporter dImp = new ExternalDBImporter();
		    		dImp.updateEmployeeTable(); // TB_EMPLOYEE update
		    		dImp.updateRoomTable(); // TB_ROOM update
		    		System.out.println("\n----- External DB syncronization ended..-----");
		    		break;	
		    	// All other arguments are taken as command script file. args = filename 	
				default: 
					m.runScriptCommand(args[0]);
					break;
			}
	    }
	}      
   /**
    * Run all commands in the script file (json format) at once 
    * Script file should be exists in our resource directory
    * SysConfig.ScriptFilePath = workingDir//resource//
    * @param fileName  (such as command.json)
    */
   private void runScriptCommand(String fileName) {	
	    System.out.println("\n----- ScriptFile execution mode is starting..-----\n");
		String filePath = SysConfig.ScriptFilePath + fileName;
		String jsonData = CommonUtil.loadJsonFile(filePath);
		if (!jsonData.equals("")) {
			CommandFactory factory = new CommandFactory();
			factory.run(jsonData);
		} else {
			System.out.println("Error: The file("+fileName+") should be in the resource directory.");
		}
		System.out.println("\n----- ScriptFile execution mode ended..-----");
	}
   /**
    * Terminate this program with exit message
    */
   private void exitMEAT() {
	   System.out.println("System stopped....");
	   System.exit(0);
   }
    /**
     * Prints the main menu and deals with user input     
     */    
    private void printMainMenu() {    	
    	System.out.println("");
        System.out.println("1. Book a meeting");
        System.out.println("2. Edit a meeting");
        System.out.println("3. Cancel a meeting");
        System.out.println("---------------------");
        System.out.println("4. Book a vacation");
        System.out.println("5. Cancel a vacation");
        System.out.println("---------------------");
        System.out.println("6. Print room's schedule");
        System.out.println("7. Print employee's schedule");
        System.out.println("8. Print company's schedule");
        System.out.println("---------------------");
        System.out.println("9. Add company holiday");
        System.out.println("0. Exit\n");
        
        //Get user input
        try {
        	int userInput = Integer.parseInt(inputOutput("Please press the task number : "));
        	
        	if (userInput >= 0 && userInput <= 9) {        		
        		switch (userInput) {        		
					case 1:
						addMeetingCommand();
						break;
					case 2:
						EditMeetingCommand();
						break;
					case 3:
						cancelMeetingCommand();
						break;
					case 4:
						addVacationCommand();
						break;
					case 5:
						cancelVacationCommand();
						break;
					case 6:
						printRoomScheduleCommand(); 
						break;
					case 7:
						printEmployeeScheduleCommand();
						break;
					case 8:
						printCompanyScheduleCommand();
						break;
					case 9:
						addCompanyHolidayCommand();
						break;
					case 0:
						exitMEAT(); 
						break;				
				}		       
        	} else {
        		System.out.println("Please enter a menu number from 0 - 8");
        		printMainMenu();
        	}
        	
        } catch (NumberFormatException e) {
        	System.out.println("Please enter a menu number from 0 - 8");
        	printMainMenu();
        }
    }    
   
    /**
     * Passes a prompt to the user and returns the user input string.
     * @param msg
     * @return String
     */
    private String inputOutput(String msg) {
        System.out.println(msg);
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    String str = "";
	    try {
	    	str = br.readLine();
	    } catch (IOException e){
	        System.out.println("An error occurs when reading system input.");
	        printMainMenu();
	    }
	    return str;
    }
    /**
     * To conform with script command handling rule, 
     * we make the same data structure (command array) and pass it to its class
     * @return void
     */
    @SuppressWarnings("unchecked")
	private void addMeetingCommand() {    	
    	JSONArray command_array = new JSONArray();    	
    	Command cmd = new Command();    	
    	// MEET DATE
    	String meetDate = inputOutput("\nPlease enter the meeting date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( meetDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(meetDate)) {
    		System.out.println("Invalid date foramt");
    		addMeetingCommand();
    		return;
    	}
    	JSONObject jDate = new JSONObject();
    	jDate.put("name", "date");
    	jDate.put("value", meetDate);
    	command_array.add(jDate);
    	
    	// MEET START TIME
    	String startTime = inputOutput("\nPlease enter the meeting startTIME (HH24:MI) or [ enter Q for mainManu ] : ");
    	if ( startTime.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkTimeValid(startTime)) {
    		System.out.println("Invalid time foramt");
    		addMeetingCommand();
    		return;
    	}    
    	JSONObject jSTime = new JSONObject();
    	jSTime.put("name", "start-time");
    	jSTime.put("value", startTime);
    	command_array.add(jSTime);
    	
    	// MEET END TIME
    	String endTime = inputOutput("\nPlease enter the meeting endTIME (HH24:MI) or [ enter Q for mainManu ] : ");
    	if ( endTime.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkTimeValid(endTime)) {
    		System.out.println("Invalid time foramt");
    		addMeetingCommand();
    		return;
    	}    	
    	JSONObject jETime = new JSONObject();
    	jETime.put("name", "end-time");
    	jETime.put("value", endTime);
    	command_array.add(jETime);
    	
    	// ROOM ID
    	String roomID = inputOutput("\nPlease enter the meeting RoomID or [ enter Q for mainManu ] : ");
    	if ( roomID.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkRoomIdValid(roomID)) {
    		System.out.println("No such roomID in the database");
    		addMeetingCommand();
    		return;
    	}
    	JSONObject jRoomID = new JSONObject();
    	jRoomID.put("name", "room-id");
    	jRoomID.put("value", roomID);
    	command_array.add(jRoomID);
    	
    	// Description 
    	String description = inputOutput("\nPlease enter the description or [ enter Q for mainManu ] : ");
    	if ( description.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkStrLenValid(description)) {
    		System.out.println("Max length limit in 1024 characters");
    		addMeetingCommand();
    		return;
    	}
    	JSONObject jDesc = new JSONObject();
    	jDesc.put("name", "description");
    	jDesc.put("value", description);
    	command_array.add(jDesc);
    	
    	// Attendee 
    	if (!addAttendeeCommand(command_array)) {
    		addMeetingCommand();
    		return;
    	}

    	/* finally input database */
    	AddMeeting addMeet = new AddMeeting(command_array);
    	String result = addMeet.execute();
    	System.out.println("### addMeeting : " + result);   
    
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	}    	
    }
    /**
     * To take multiple attendees, call this function recursively
     * @param command_array
     * @return
     */    
	@SuppressWarnings("unchecked")
	private boolean addAttendeeCommand(JSONArray command_array) {
    	
    	Command cmd = new Command();    	
    	String attendeeID = inputOutput("\nPlease enter the employeeID to attend (delete) or [ enter Q for mainManu ] : ");
    	
    	if ( attendeeID.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return false;
    	}
    	if (!cmd.checkEmpolyeeIdValid(attendeeID)) {
    		System.out.println("No such employeeID in the database");
    		addMeetingCommand();
    		return false;
    	}
    	JSONObject jAttendee = new JSONObject();
    	jAttendee.put("name", "attendee");
    	jAttendee.put("value", attendeeID);
    	command_array.add(jAttendee);
    	
    	String moreAttendee = inputOutput("\nTo more attendees ? Y or N : ");
    	if (moreAttendee.equalsIgnoreCase("Y")) {
    		return addAttendeeCommand(command_array);
    	} 	    	
    	return true;
    }
	
	/**
     * Edit meeting command array as same as command line script   
     * @return void
     */
    @SuppressWarnings("unchecked")
	private void EditMeetingCommand() {   
    	
    	JSONArray command_array = new JSONArray();       
    	
    	Command cmd = new Command();    	
    	// MEET ID
    	String meetID = inputOutput("\nPlease enter the meeting ID to edit or [ enter Q for mainManu ] : ");
    	if ( meetID.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkMeetingIdValid(meetID)) {
    		System.out.println("The meetID not in database");
    		EditMeetingCommand();
    		return;
    	}
    	/*Current meeting information screen print */
    	Meeting mt = new Meeting();
    	mt.getMeetingInfo(meetID);
    	mt.printCurrentMeetingInfo();
    	
    	JSONObject jEditID = new JSONObject();
    	jEditID.put("name", "meeting-id");
    	jEditID.put("value", meetID);
    	command_array.add(jEditID);
    	// Attendee edit ? 
    	String editMode = inputOutput("\nAdd a attendee (Press A), Delete a attendee(Press D) or Edit meeting detail (Press E) : ");
    
    	switch (editMode.toUpperCase()) {
		case "A":
			// Attendee 
	    	if (!addAttendeeCommand(command_array)) {
	    		EditMeetingCommand();
	    		return;
	    	}
	    	/* finally input database */
	    	EditMeeting editAddAteendee = new EditMeeting(command_array, "ADD");
	    	//System.out.println(command_array.toJSONString());
	    	String result = editAddAteendee.execute();
	    	System.out.println("### editMeeting(addAttendee) : " + result); 	
			break;
		case "D":
			// Attendee 
	    	if (!addAttendeeCommand(command_array)) {
	    		EditMeetingCommand();
	    		return;
	    	}
	    	/* finally input database */
	    	EditMeeting editRmAteendee = new EditMeeting(command_array, "REMOVE");
	    	result = editRmAteendee.execute();
	    	System.out.println("### editMeeting(removeAttendee) : " + result); 			
			break;
		case "E":			
			// MEET DATE
	    	String meetDate = inputOutput("\nPlease enter the meeting date (MMDDYYYY) or [ enter S to skip ] : ");
	    	if ( !meetDate.equalsIgnoreCase("S") ) {	    		
		    	if (!cmd.checkDateValid(meetDate)) {
		    		System.out.println("Invalid date foramt");
		    		addMeetingCommand();
		    		return;
		    	}
		    	JSONObject jDate = new JSONObject();
		    	jDate.put("name", "date");
		    	jDate.put("value", meetDate);
		    	command_array.add(jDate);
	    	}	    	
	    	// MEET START TIME
	    	String startTime = inputOutput("\nPlease enter the meeting startTIME (HH24:MI) or [ enter S to skip ] : ");
	    	if ( !startTime.equalsIgnoreCase("S")) {
		    	if (!cmd.checkTimeValid(startTime)) {
		    		System.out.println("Invalid time foramt");
		    		addMeetingCommand();
		    		return;
		    	}    
		    	JSONObject jSTime = new JSONObject();
		    	jSTime.put("name", "start-time");
		    	jSTime.put("value", startTime);
		    	command_array.add(jSTime);
	    	}
	    	
	    	// MEET END TIME
	    	String endTime = inputOutput("\nPlease enter the meeting endTIME (HH24:MI) or [ enter S to skip ] : ");
	    	if ( !endTime.equalsIgnoreCase("S") ) {	    	
		    	if (!cmd.checkTimeValid(endTime)) {
		    		System.out.println("Invalid time foramt");
		    		addMeetingCommand();
		    		return;
		    	}    	
		    	JSONObject jETime = new JSONObject();
		    	jETime.put("name", "end-time");
		    	jETime.put("value", endTime);
		    	command_array.add(jETime);
	    	}	
	    	// ROOM ID
	    	String roomID = inputOutput("\nPlease enter the meeting RoomID or [ enter S to skip ] : ");
	    	if ( !roomID.equalsIgnoreCase("S") ) {		    	
		    	if (!cmd.checkRoomIdValid(roomID)) {
		    		System.out.println("No such roomID in the database");
		    		addMeetingCommand();
		    		return;
		    	}
		    	JSONObject jRoomID = new JSONObject();
		    	jRoomID.put("name", "room-id");
		    	jRoomID.put("value", roomID);
		    	command_array.add(jRoomID);
	    	}
	    	// Description 
	    	String description = inputOutput("\nPlease enter the description or [ enter S to skip ] : ");
	    	if ( !description.equalsIgnoreCase("S")) {	    		
		    	if (!cmd.checkStrLenValid(description)) {
		    		System.out.println("Max length limit in 1024 characters");
		    		addMeetingCommand();
		    		return;
		    	}
		    	JSONObject jDesc = new JSONObject();
		    	jDesc.put("name", "description");
		    	jDesc.put("value", description);
		    	command_array.add(jDesc);
	    	}	    	
	    	/* finally input database */
	    	EditMeeting editDetail = new EditMeeting(command_array);
	    	result = editDetail.execute();
	    	System.out.println("### editMeeting(meetingDetail) : " + result); 			
			break;
		default:
			System.out.println("Unknown key pressed");
    		EditMeetingCommand();
			break;
		}
    	    
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	}       	
    }	
	 /**
     * Cancel meeting command array as same as command line script   
     * @return JSONArray
     */
    @SuppressWarnings("unchecked")
	private void cancelMeetingCommand() {   
    	
    	JSONArray command_array = new JSONArray();   
    	
    	Command cmd = new Command();    	
    	// MEET ID
    	String meetID = inputOutput("\nPlease enter the meeting ID to cancel or [ enter Q for mainManu ] : ");
    	if ( meetID.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkMeetingIdValid(meetID)) {
    		System.out.println("The meetID not in database");
    		cancelMeetingCommand();
    		return;
    	}
    	JSONObject jCancelID = new JSONObject();
    	jCancelID.put("name", "meeting-id");
    	jCancelID.put("value", meetID);
    	command_array.add(jCancelID);
    	    	
    	/* finally input database */
    	CancelMeeting cancelMeet = new CancelMeeting(command_array);
    	String result = cancelMeet.execute();
    	System.out.println("### cancelMeeting : " + result);   
    
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	}    
    	
    }    
    /**
     * To allow employees to schedule their vacation 
     * also checking previous meeting schedules
     * @return void
     */
    @SuppressWarnings("unchecked")
	private void addVacationCommand() {    	
    	JSONArray command_array = new JSONArray();    	
    	Command cmd = new Command();    	
    	
    	String employeeID = inputOutput("\nPlease enter the employeeID to book a vacation or [ enter Q for mainManu ] : ");
    	
    	if ( employeeID.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkEmpolyeeIdValid(employeeID)) {
    		System.out.println("No such employeeID in the database");
    		addVacationCommand();
    		return;
    	}
    	JSONObject jEmployee = new JSONObject();
    	jEmployee.put("name", "employee-id");
    	jEmployee.put("value", employeeID);
    	command_array.add(jEmployee);
    	
    	// VACATION START DATE
    	String vacSDate = inputOutput("\nPlease enter the start date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( vacSDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(vacSDate)) {
    		System.out.println("Invalid date foramt");
    		addVacationCommand();
    		return;
    	}
    	JSONObject jSDate = new JSONObject();
    	jSDate.put("name", "start-date");
    	jSDate.put("value", vacSDate);
    	command_array.add(jSDate);
    	
    	// VACATION END DATE
    	String vacEDate = inputOutput("\nPlease enter the end date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( vacEDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(vacSDate)) {
    		System.out.println("Invalid date foramt");
    		addVacationCommand();
    		return;
    	}
    	JSONObject jEDate = new JSONObject();
    	jEDate.put("name", "end-date");
    	jEDate.put("value", vacEDate);
    	command_array.add(jEDate);
    	
    	/* finally input database */
    	AddVacation addVac = new AddVacation(command_array);
    	String result = addVac.execute();
    	System.out.println("### addVacation : " + result);   
    
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	}    	
    }
    
    /**
     * Cancel vacation of an employee   
     * @return JSONArray
     */
    @SuppressWarnings("unchecked")
	private void cancelVacationCommand() {   
    	
    	JSONArray command_array = new JSONArray();
    	Command cmd = new Command();    	
    	
    	String employeeID = inputOutput("\nPlease enter the employeeID to cancel vacations or [ enter Q for mainManu ] : ");
    	
    	if ( employeeID.equalsIgnoreCase("Q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkEmpolyeeIdValid(employeeID)) {
    		System.out.println("No such employeeID in the database");
    		cancelVacationCommand();
    		return;
    	}
    	JSONObject jEmployee = new JSONObject();
    	jEmployee.put("name", "employee-id");
    	jEmployee.put("value", employeeID);
    	command_array.add(jEmployee);
    	
    	Vacation vac = new Vacation();
    	vac.printScreenVacationList(employeeID);
    	String goAhead = inputOutput("\nAre you sure to cancel this vacation ? Y or [ enter Q for mainManu ] : ");
    	if ( !goAhead.equalsIgnoreCase("Y") ) {
    		printMainMenu();
    		return;
    	}  
    	 
    	/* finally input database */
    	CancelVacation cancelVac = new CancelVacation(command_array);
    	String result = cancelVac.execute();
    	System.out.println("### cancelVacation : " + result);   
    
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	}    
    	
    }
    /*
     * print a room meeting schedule for specific date range
     * allow option for print screen or save file
     */    
	private void printRoomScheduleCommand() {    	
    	
    	Command cmd = new Command();
    	
    	String roomID = inputOutput("\nPlease enter the roomID for printing schedules or [ enter Q for mainManu ] : ");
    	
    	if ( roomID.equalsIgnoreCase("Q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkRoomIdValid(roomID)) {
    		System.out.println("No such roomID in the database");
    		printRoomScheduleCommand();
    		return;
    	}
    	
    	// Search START DATE
    	String srchStartDate = inputOutput("\nPlease enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchStartDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchStartDate)) {
    		System.out.println("Invalid date foramt");
    		printRoomScheduleCommand();
    		return;
    	}
    	
    	// Search End DATE
    	String srchEndDate = inputOutput("\nPlease enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchEndDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchEndDate)) {
    		System.out.println("Invalid date foramt");
    		printRoomScheduleCommand();
    		return;
    	}
    	
    	String printMode = inputOutput("\nPrint screen (Press S) or Save File (Press F)  or [ enter Q for mainManu ] : ");
    	
    	switch (printMode.toUpperCase()) {
			case "S":
				PrintScheduleRoom psrs = new PrintScheduleRoom();
				psrs.setSrchStartDay(srchStartDate);
				psrs.setSrchEndDay(srchEndDate);
				Room roomS = psrs.getRoom();
				roomS.getRoomInfo(roomID);
				psrs.setRoom(roomS);
				psrs.printScreenRoomSchedule();
				break;
			case "F":
				String fileName = inputOutput("\nPlease enter file name : ");
				PrintScheduleRoom psrf = new PrintScheduleRoom();
				psrf.setSrchStartDay(srchStartDate);
				psrf.setSrchEndDay(srchEndDate);
				Room roomF = psrf.getRoom();
				roomF.getRoomInfo(roomID);
				psrf.setRoom(roomF);
				psrf.setOutfileName(fileName);
				psrf.printFileRoomSchedule();
				break;
			case "Q":
				printMainMenu();
				break;
			default:
				System.out.println("Unknown command key");
				printMainMenu();
				break;
		}   
    	
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	} 
    }
    
    /**
     * print a employee schedule for specific date range
     * allow option for print screen or save file
     */
    private void printEmployeeScheduleCommand() {
    	
    	Command cmd = new Command();
    	
    	String empID = inputOutput("\nPlease enter the employeeID for printing schedules or [ enter Q for mainManu ] : ");
    	
    	if ( empID.equalsIgnoreCase("Q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkEmpolyeeIdValid(empID)) {
    		System.out.println("No such Employee ID in the database");
    		printEmployeeScheduleCommand();
    		return;
    	}
    	
    	// Search START DATE
    	String srchStartDate = inputOutput("\nPlease enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchStartDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchStartDate)) {
    		System.out.println("Invalid date foramt");
    		printEmployeeScheduleCommand();
    		return;
    	}
    	
    	// Search End DATE
    	String srchEndDate = inputOutput("\nPlease enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchEndDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchEndDate)) {
    		System.out.println("Invalid date foramt");
    		printEmployeeScheduleCommand();
    		return;
    	}
    	
    	String printMode = inputOutput("\nPrint screen (Press S) or Save File (Press F)  or [ enter Q for mainManu ] : ");
    	
    	switch (printMode.toUpperCase()) {
			case "S":
				PrintScheduleEmployee pses = new PrintScheduleEmployee();
				pses.setSrchStartDay(srchStartDate);
				pses.setSrchEndDay(srchEndDate);
				Employee empS = pses.getEmployee();
				empS.getPersonInfo(empID);  // information setting
				pses.setEmployee(empS);
				pses.printScreenEmployeeSchedule();
				break;
			case "F":
				String fileName = inputOutput("\nPlease enter file name : ");
				PrintScheduleEmployee psef = new PrintScheduleEmployee();
				psef.setSrchStartDay(srchStartDate);
				psef.setSrchEndDay(srchEndDate);
				Employee empF = psef.getEmployee();
				empF.getPersonInfo(empID);  // information setting
				psef.setEmployee(empF);
				psef.setOutfileName(fileName);
				psef.printFileEmployeeSchedule();
				break;
			case "Q":
				printMainMenu();
				break;
			default:
				System.out.println("Unknown command key");
				printMainMenu();
				break;
		}   
    	
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	} 
    	
    }
    /**
     * Print all company's meeting schedules
     */
    private void printCompanyScheduleCommand() {
    	
    	Command cmd = new Command();
    	
    	// Search START DATE
    	String srchStartDate = inputOutput("\nPlease enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchStartDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchStartDate)) {
    		System.out.println("Invalid date foramt");
    		printEmployeeScheduleCommand();
    		return;
    	}
    	
    	// Search End DATE
    	String srchEndDate = inputOutput("\nPlease enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( srchEndDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(srchEndDate)) {
    		System.out.println("Invalid date foramt");
    		printEmployeeScheduleCommand();
    		return;
    	}
    	
    	String printMode = inputOutput("\nPrint screen (Press S) or Save File (Press F)  or [ enter Q for mainManu ] : ");
    	
    	switch (printMode.toUpperCase()) {
			case "S":
				PrintScheduleAll psea = new PrintScheduleAll();
				psea.setSrchStartDay(srchStartDate);
				psea.setSrchEndDay(srchEndDate);
				psea.printScreenAllCompanySchedule();
				break;
			case "F":
				String fileName = inputOutput("\nPlease enter file name : ");
				PrintScheduleAll psaf = new PrintScheduleAll();
				psaf.setSrchStartDay(srchStartDate);
				psaf.setSrchEndDay(srchEndDate);				
				psaf.setOutfileName(fileName);
				psaf.printFileAllCompanySchedule();
				break;
			case "Q":
				printMainMenu();
				break;
			default:
				System.out.println("Unknown command key");
				printMainMenu();
				break;
		}   
    	
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	} 
    	
    }
    
    /**
     * Add company's holiday
     */
    private void addCompanyHolidayCommand() {
    	
    	Command cmd = new Command();
    	
    	// holiday START DATE
    	String startDate = inputOutput("\nPlease enter the holiday start date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( startDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(startDate)) {
    		System.out.println("Invalid date foramt");
    		addCompanyHolidayCommand();
    		return;
    	}
    	
    	// holiday End DATE
    	String endDate = inputOutput("\nPlease enter the holiday end date (MMDDYYYY) or [ enter Q for mainManu ] : ");
    	if ( endDate.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkDateValid(endDate)) {
    		System.out.println("Invalid date foramt");
    		addCompanyHolidayCommand();
    		return;
    	}
    	
    	// Description 
    	String description = inputOutput("\nPlease enter the description or [ enter Q for mainManu ] : ");
    	if ( description.equalsIgnoreCase("q")) {
    		printMainMenu();
    		return;
    	}
    	if (!cmd.checkStrLenValid(description)) {
    		System.out.println("Max length limit in 1024 characters");
    		addCompanyHolidayCommand();
    		return;
    	}
    	
    	/* finally input database */
    	AddHoliday addHday = new AddHoliday();
    	addHday.setStartDate(startDate);
    	addHday.setEndDate(endDate);
    	addHday.setDescription(description);
    	String result = SysConfig.fail;
    	if (addHday.insertHolidayInfo()) {
    		result = SysConfig.success;
    	}
    	System.out.println("### addHoliday : " + result);       	
    	
    	String goMain = inputOutput("\nTo go mainMenu perss Y or N to Exit ");
    	
    	if (goMain.equalsIgnoreCase("Y")) {
    		printMainMenu();
    	} else {
    		System.exit(0);
    	} 
    	
    }
		
}
	
