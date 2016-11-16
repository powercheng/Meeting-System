package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CommonUtil;
import common.SysConfig;
import controller.AddMeeting;
import controller.CancelMeeting;
import controller.Command;
import controller.CommandFactory;
import controller.PrintScheduleAll;
import controller.PrintScheduleRoom;

public class Main {	
	/**
     * MEAT System begins !
     * 1. "No args" is interactive mode
     * 2. "args = scriptname" is script running mode
     * 3. "args = jsonDBfile" is to load external db into local db
     * @param args 
     */
    public static void main(String[] args) {
    	
	    System.out.println("*** Welcome to the MEAT ! ***");
	        
	    Main m = new Main();
	    if (args.length == 0) {
	    	// no passing parameter means interactive mode
	    	m.printMainMenu();
	    } else {
	    	switch (args[0]) {
	    		// External DB (employee.json and room.json in resource dir) loaded into local database
		    	case "DataSynchronize":   
					/* TO DO */
		    		ExternalDBImporter dImp = new ExternalDBImporter();
		    		dImp.updateEmployeeTable(); // TB_EMPLOYEE update
		    		dImp.updateEmployeeTable(); // TB_ROOM update
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
    * @param fileName  (such as command.json)
    */
   private void runScriptCommand(String fileName) {		
		String filePath = SysConfig.runningDir + "\\" + fileName;
		String jsonData = CommonUtil.loadJsonFile(filePath);
		CommandFactory factory = new CommandFactory();
		factory.run(jsonData);
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
        System.out.println("0. Exit\n");
        
        //Get user input
        try {
        	int userInput = Integer.parseInt(inputOutput("Please press the task number : "));
        	
        	if (userInput >= 0 && userInput <= 8) {        		
        		switch (userInput) {        		
					case 1:
						addMeetingCommand();
						break;
					case 2:
						System.out.println("edit a meeting");
						break;
					case 3:
						cancelMeetingCommand();
						break;
					case 4:
						System.out.println("book a vacation");
						break;
					case 5:
						System.out.println("cancel a vacation");
						break;
					case 6:
						// to do 
						break;
					case 7:
						// to do 
						break;
					case 8:
						// to do 
						break;
					case 0:
						System.exit(0); 
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
     * @return JSONArray
     */
    @SuppressWarnings("unchecked")
	private void addMeetingCommand() {    	
    	JSONArray command_array = new JSONArray();    	
    	Command cmd = new Command();    	
    	// MEET DATE
    	String meetDate = inputOutput("\nPlease enter the meeting date (MMDDYYYY) or [ enter q for mainManu ] : ");
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
    	String startTime = inputOutput("\nPlease enter the meeting startTIME (HH24:MI) or [ enter q for mainManu ] : ");
    	if ( meetDate.equalsIgnoreCase("q")) {
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
    	String endTime = inputOutput("\nPlease enter the meeting endTIME (HH24:MI) or [ enter q for mainManu ] : ");
    	if ( meetDate.equalsIgnoreCase("q")) {
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
    	String roomID = inputOutput("\nPlease enter the meeting RoomID or [ enter q for mainManu ] : ");
    	if ( meetDate.equalsIgnoreCase("q")) {
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
    	String description = inputOutput("\nPlease enter the description or [ enter q for mainManu ] : ");
    	if ( meetDate.equalsIgnoreCase("q")) {
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
    	String attendeeID = inputOutput("\nPlease enter the employeeID to attend or [ enter q for mainManu ] : ");
    	
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
     * Cancel meeting command array as same as command line script   
     * @return JSONArray
     */
    @SuppressWarnings("unchecked")
	private void cancelMeetingCommand() {   
    	
    	JSONArray command_array = new JSONArray();   
    	
    	/*Current schedule list on user interface */
    	PrintScheduleAll pSchedule = new PrintScheduleAll();
    	/* scheduled meeting listing */
    	String srchStartDay = CommonUtil.getAddDayStringFromNow("MMddyyyy", 0); // today
    	String srchEndDay   = CommonUtil.getAddDayStringFromNow("MMddyyyy", 30);  // today + 30 days
    	pSchedule.setSrchStartDay(srchStartDay);    	
    	pSchedule.setSrchEndDay(srchEndDay);
    	JSONObject rTobObj = pSchedule.getAllCompanyScheduleList();
    	JSONArray  rMeetList = (JSONArray) rTobObj.get("events");
    	System.out.println("--Current scheduled meeting within 30 days -- ");
    	System.out.println("#MeetID   #Meeting Time           #RoomID ");
    	System.out.println("---------------------------------------------");
    	for (int i=0; i<rMeetList.size();i++) {
    		JSONObject rSubObj = (JSONObject) rMeetList.get(i);
    		String saveMID = (String) rSubObj.get("meeting-id");
    		String saveDate = (String) rSubObj.get("date");
    		String saveSTime = (String) rSubObj.get("start-time");
    		String saveETime = (String) rSubObj.get("end-time");
    		String saveRID = (String) rSubObj.get("room-id");
    		System.out.println("# " + saveMID + "       " + CommonUtil.dateFormat(saveDate,"MMddyyyy","MM-dd-yy") 
    								+ " " + saveSTime + "-" + saveETime + "     " + saveRID);
    	}
    	System.out.println("---------------------------------------------");
    	
    	Command cmd = new Command();    	
    	// MEET ID
    	String meetID = inputOutput("\nPlease enter the meeting ID to cancel or [ enter q for mainManu ] : ");
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
		
}
	
