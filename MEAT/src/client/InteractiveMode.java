package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.SysConfig;
import controller.AddHoliday;
import controller.AddMeeting;
import controller.AddVacation;
import controller.CancelMeeting;
import controller.CancelVacation;
import controller.Command;
import controller.EditMeeting;
import controller.PrintScheduleAll;
import controller.PrintScheduleEmployee;
import controller.PrintScheduleRoom;

public class InteractiveMode {
	
    private static String inputOutput(String msg) {
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
	
		// process the interactive mode 0-11 commands	
		// print out questions for each commands
		// store the pair of names and values into the JSONArray
		// pass JSONArray to the command, it uses the same interface with script mode
		// for the name : attendee, we can store the multiple attendees' id into one string, separate by space.
    public static void printMainMenu() {    	
    	System.out.println("# Interactive mode is running (Menu Below)#\n");
        System.out.println("1. Book a meeting");
        System.out.println("2. edit-meeting-details");
        System.out.println("3. edit-meeting-add-attendees");
        System.out.println("4. edit-meeting-remove-attendees");
        System.out.println("5. Cancel a meeting");
        System.out.println("---------------------");
        System.out.println("6. Book a vacation");
        System.out.println("7. Cancel a vacation");
        System.out.println("---------------------");
        System.out.println("8. Print room's schedule");
        System.out.println("9. Print employee's schedule");
        System.out.println("10. Print company's schedule");
        System.out.println("---------------------");
        System.out.println("11. Add company holiday");
        System.out.println("---------------------");
        System.out.println("0. Exit\n");
        String res = null;
        //Get user input
        try {
        	int userInput = Integer.parseInt(inputOutput("Please press the task number : "));
        	Command command = null;
        	JSONArray command_array = null;
        	if (userInput >= 0 && userInput <= 9) {        		
        		switch (userInput) {        		
					case 1:
						String[] questions = {
								"Please enter the meeting date (MMDDYYYY) or [ enter Q for mainManu ] : ",
						    	"Please enter the meeting startTIME (HH24:MI) or [ enter Q for mainManu ] : ",
						    	"Please enter the meeting endTIME (HH24:MI) or [ enter Q for mainManu ] : ",
						    	"Please enter the meeting RoomID or [ enter Q for mainManu ] : ",
						    	"Please enter the description or [ enter Q for mainManu ] : ",
						    	"Please enter the employeeIDs, separate by space, to attend (delete) or [ enter Q for mainManu ] : "
						};
						String[] values = askQuestions(questions);
						String[] names = {
								"date","start-time","end-time","room-id","description","attendee"
						};
						command_array = changeToJson(names,values);
						command = new AddMeeting(command_array);							
						break;
					case 2:
						String[] questions1 = {
								"Please enter the meeting ID to edit or [ enter Q for mainManu ] : ",
								"Please enter the meeting date (MMDDYYYY) or [ enter S to skip ] : ",
								"Please enter the meeting startTIME (HH24:MI) or [ enter S to skip ] : ",
								"Please enter the meeting endTIME (HH24:MI) or [ enter S to skip ] : ",
								"Please enter the meeting RoomID or [ enter S to skip ] : ",
								"Please enter the description or [ enter S to skip ] : "
						};
						String[] values1 = askQuestions(questions1);
						String[] names1 = {
								"meeting-id","date","start-time","end-time","room-id","description"
						};
						command_array = changeToJson(names1,values1);
						command = new EditMeeting(command_array);							
						break;
					case 3:
						String[] questions2 = {
								"Please enter the meeting ID to edit or [ enter Q for mainManu ] : ",
								"Please enter the employeeIDs, separate by space, to attend or [ enter Q for mainManu ] : "
						};
						String[] values2 = askQuestions(questions2);
						String[] names2 = {
								"meeting-id","attendee"
						};
						command_array = changeToJson(names2,values2);
						command = new EditMeeting(command_array, SysConfig.addTag); // Attendee option = add	
						break;
					case 4:
						String[] questions3 = {
								"Please enter the meeting ID to edit or [ enter Q for mainManu ] : ",
								"Please enter the employeeIDs, separate by space, to delete or [ enter Q for mainManu ] : "
						};
						String[] values3 = askQuestions(questions3);
						String[] names3 = {
								"meeting-id","attendee"
						};
						command_array = changeToJson(names3,values3);
						command = new EditMeeting(command_array, SysConfig.removeTag); // Attendee option = remove							
						break;
					case 5:
						String[] questions4 = {
								"Please enter the meeting ID to cancel or [ enter Q for mainManu ] : "
						};
						String[] values4 = askQuestions(questions4);
						String[] names4 = {
								"meeting-id"
						};
						command_array = changeToJson(names4,values4);
						command = new CancelMeeting(command_array);							
						break;
					case 6:
						String[] questions5 = {
								"Please enter the employeeID to book a vacation or [ enter Q for mainManu ] : ",
								"Please enter the start date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the end date (MMDDYYYY) or [ enter Q for mainManu ] : "
						};
						String[] values5 = askQuestions(questions5);
						String[] names5 = {
								"employee-id","start-date","end-date"
						};
						command_array = changeToJson(names5,values5);
						command = new AddVacation(command_array);							
						break;
					case 7:
						String[] questions6 = {
								"Please enter the employeeID to cancel vacations or [ enter Q for mainManu ] : "
						};
						String[] values6 = askQuestions(questions6);
						String[] names6 = {
								"employee-id"
						};
						command_array = changeToJson(names6,values6);
						command = new CancelVacation(command_array);							
						break;
					case 8:
						String[] questions7 = {
								"Please enter the roomID to print schedule or [ enter Q for mainManu ] : ",
								"Please enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please give the output file name  or [ enter Q for mainManu ] :  "
						};
						String[] values7 = askQuestions(questions7);
						String[] names7 = {
								"room-id","start-date","end-date","output-file"
						};
						command_array = changeToJson(names7,values7);
						command = new PrintScheduleRoom(command_array);	
													
						break;
					case 9:
						String[] questions8 = {
								"Please enter the employeeID to print schedule or [ enter Q for mainManu ] : ",
								"Please enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please give the output file name  or [ enter Q for mainManu ] : "
						};
						String[] values8 = askQuestions(questions8);
						String[] names8 = {
								"employee-id","start-date","end-date","output-file"
						};
						command_array = changeToJson(names8,values8);
						command = new PrintScheduleEmployee(command_array);	
													
						break;
					case 10:
						String[] questions9 = {
								"Please enter the search start date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the search end date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Print screen (Press S) or Save File (Press F)  or [ enter Q for mainManu ] : "
						};
						String[] values9 = askQuestions(questions9);
						String[] names9 = {
								"start-date","end-date","output-file"
						};
						command_array = changeToJson(names9,values9);
						command = new PrintScheduleAll(command_array);						
						break;
					case 11:
						String[] questions10 = {
								"Please enter the holiday start date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the holiday end date (MMDDYYYY) or [ enter Q for mainManu ] : ",
								"Please enter the description or [ enter Q for mainManu ] : "
						};
						String[] values10 = askQuestions(questions10);
						String[] names10 = {
								"start-date","end-date","description"
						};
						command_array = changeToJson(names10,values10);
						command = new AddHoliday(command_array);
						break;
					case 0:
						System.exit(0); 
						break;				
				}
        		if(command != null) {
					String result = command.execute();
					System.out.println("### command : " + result);				
				}	
        	} else {
        		System.out.println("Please enter a menu number from 0 - 8");       		
        	}
        	
        } catch (NumberFormatException e) {
        	System.out.println("Please enter a menu number from 0 - 8");
        }         
        printMainMenu();
    }    
	

	private static String[] askQuestions(String[] strs) {
		if (strs == null || strs.length == 0)
			return null;
		String[] res = new String[strs.length];

		try {
			BufferedReader strin = new BufferedReader(new InputStreamReader(
					System.in));
			for (int i = 0; i < strs.length; i++) {
				System.out.println(strs[i]);
				res[i] = strin.readLine();
				if(res[i].equalsIgnoreCase("q")){
					printMainMenu();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray changeToJson(String[] names, String[] values) {
		JSONArray command_array = new JSONArray();
		for(int i = 0; i < names.length; i++) {
			if(values[i].equalsIgnoreCase("s")) continue;
			JSONObject jDate = new JSONObject();
	    	jDate.put("name", names[i]);
	    	jDate.put("value", values[i]);
	    	command_array.add(jDate);
		}		
		return command_array;
	}
	









}
