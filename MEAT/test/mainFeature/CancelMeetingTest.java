package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import controller.AddMeeting;
import controller.CancelMeeting;
import exceptions.AddMeetingException;
import exceptions.CancelMeetingException;

public class CancelMeetingTest {

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
	public void testCancelMeetingTC_F3_1() {
		/* Pre condition - add a new meeting information with id = 1(first insert) */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
		
		AddMeeting addmeet = new AddMeeting(command_array);
		boolean noException  = true;
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}
		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");		
		CancelMeeting canmeet = new CancelMeeting(command_array);
		try {
			canmeet.execute();
		} catch (CancelMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);			
	}
	
	@Test (expected=CancelMeetingException.class)
	public void testCancelMeetingTC_F3_2() throws Exception {
		/* Test if no such classifier name */
		initCommand();	
		buildCommand("meet-id", "1");		
		CancelMeeting canmeet = new CancelMeeting(command_array);
		canmeet.execute();						
	}
	
	@Test (expected=CancelMeetingException.class)
	public void testCancelMeetingTC_F3_3() throws Exception {
		/* Test if no such meeting id in the database */
		initCommand();	
		buildCommand("meeting-id", "10");		
		CancelMeeting canmeet = new CancelMeeting(command_array);
		canmeet.execute();						
	}
	
	@Test (expected=CancelMeetingException.class)
	public void testCancelMeetingTC_F3_4() throws Exception {
		/* Test if empty input */
		initCommand();	
		CancelMeeting canmeet = new CancelMeeting(command_array);
		canmeet.execute();						
	}

}
