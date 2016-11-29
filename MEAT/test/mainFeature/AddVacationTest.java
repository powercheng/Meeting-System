package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import controller.AddMeeting;
import controller.AddVacation;
import exceptions.AddVacationException;

public class AddVacationTest {
	
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
	public void testAddVacationTC_F4_1() {
		/* test if add vacation successfully */
		initCommand();		
		buildCommand("employee-id", "bob099");
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");
			
		AddVacation addvac = new AddVacation(command_array);
		boolean noException  = true;
		
		try {
			addvac.execute();
		} catch (AddVacationException avex) {
			noException = false;
		}
		
		assertTrue(noException);
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_2() throws Exception {
		/* Test if input is empty */
		initCommand();			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_3() throws Exception {
		/* Test if invalid classifier name */
		initCommand();		
		buildCommand("emp-id", "bob099");  // invalid
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_4() throws Exception {
		/* Test if no such employee id in the database */
		initCommand();		
		buildCommand("employee-id", "test001");  // invalid
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_5() throws Exception {
		/* Test if no such employee id in the database */
		initCommand();		
		buildCommand("employee-id", "bob099");  
		buildCommand("start-date",  "12322016"); // invalid
		buildCommand("end-date",    "12072016");			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_6() throws Exception {
		/* Test if no such employee id in the database */
		initCommand();		
		buildCommand("employee-id", "bob099");  
		buildCommand("start-date",  "12052016"); 
		buildCommand("end-date",    "13072016"); // invalid			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}
	
	@Test (expected=AddVacationException.class)
	public void testCancelMeetingTC_F3_7() throws Exception {
		
		/* Pre condition - add a meeting having conflict time */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099");		
		
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();
		
		/* Test if having conflict with previous scheduled meeting time */
		initCommand();		
		buildCommand("employee-id", "bob099");  
		buildCommand("start-date",  "12052016"); 
		buildCommand("end-date",    "12072016"); 			
		AddVacation addvac = new AddVacation(command_array);
		addvac.execute();						
	}

}
