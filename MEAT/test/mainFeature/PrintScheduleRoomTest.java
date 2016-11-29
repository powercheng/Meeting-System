package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import controller.AddMeeting;
import controller.PrintScheduleRoom;
import exceptions.AddMeetingException;
import exceptions.PrintScheduleRoomException;

public class PrintScheduleRoomTest {

	JSONArray command_array;
	
	@Before
	public void setUp() throws Exception {
		command_array = new JSONArray();
	}

	@After
	public void tearDown() throws Exception {
		CommonUtil.initDB();
		command_array = null;
	}
	
	public void initCommand() {
		command_array = new JSONArray();		
	}
	
	@SuppressWarnings("unchecked")
	public void buildCommand(String name, String value) {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("value", value);
		command_array.add(obj);
	}

	@Test
	public void testPrintScheduleRoomTC_F6_1() {
		
		/* Pre condition = save a meeting information*/
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
		
		AddMeeting addmeet = new AddMeeting(command_array);
		boolean noException  = true;
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if print room(3A66) schedule successfully */
		initCommand();
		buildCommand("room-id",     "3A66");
		buildCommand("start-date",  "12012016");
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "test2.json");
		
		PrintScheduleRoom  psr = new PrintScheduleRoom(command_array);
		try {
			psr.execute();
		} catch (PrintScheduleRoomException psrex) {
			noException = false;
		}		
		assertTrue(noException);	
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_2() throws Exception {
		/* Test if no such rood id in the database */
		initCommand();	
		buildCommand("room-id",     "3X66"); // invalid
		buildCommand("start-date",  "12012016");
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_3() throws Exception {
		/* Test if start-date is out of date range */
		initCommand();	
		buildCommand("room-id",     "3A66"); 
		buildCommand("start-date",  "12322016"); // invalid
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_4() throws Exception {
		/* Test if start-date is empty */
		initCommand();	
		buildCommand("room-id",     "3A66"); 
		buildCommand("start-date",  ""); // empty
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_5() throws Exception {
		/* Test if end-date is out of date range */
		initCommand();	
		buildCommand("room-id",     "3A66"); 
		buildCommand("start-date",  "12012016"); 
		buildCommand("end-date",    "00202016");// invalid
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_6() throws Exception {
		/* Test if end-date is empty */
		initCommand();	
		buildCommand("room-id",     "3A66"); 
		buildCommand("start-date",  "12012016"); 
		buildCommand("end-date",    "");// empty
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_7() throws Exception {
		/* Test if output filename is empty */
		initCommand();	
		buildCommand("room-id",     "3A66"); 
		buildCommand("start-date",  "12012016"); 
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "");	 // empty	
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}
	
	@Test (expected=PrintScheduleRoomException.class)
	public void testPrintScheduleRoomTC_F6_8() throws Exception {
		/* Test if invalid classifier name */
		initCommand();	
		buildCommand("room",        "3A66"); // invalid classifier
		buildCommand("start-date",  "12012016"); 
		buildCommand("end-date",    "12202016");
		buildCommand("output-file", "test2.json");		
		PrintScheduleRoom psr = new PrintScheduleRoom(command_array);
		psr.execute();						
	}


}
