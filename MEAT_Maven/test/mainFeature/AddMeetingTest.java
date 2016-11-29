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
import exceptions.AddMeetingException;
/**
 * Test AddMeeting feature. 
 * @author group7
 */
public class AddMeetingTest {
	
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
	public void testAddMeetingTC_F1_1() {
		/* Test command data build */
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
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_2() throws AddMeetingException {
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "13062016");  // not valid
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();
			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_3() throws AddMeetingException {
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "25:30");  // not valid
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();
			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_4() throws AddMeetingException {
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");  
		buildCommand("end-time",    "12:80"); // not valid
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();
			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_5() throws AddMeetingException {
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3X75");  // not valid
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_6() throws AddMeetingException {
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");  
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxt067 bob099");		// gayxt067 not valid
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_7() throws AddMeetingException {
		/* Precondition : booked same time in the same room before testing */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");  
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");
		AddMeeting preMeet = new AddMeeting(command_array);
		preMeet.execute();
		
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:40");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");  
		buildCommand("description", "Another meeting");
		buildCommand("attendee",    "tolas9999 desil1337");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_8() throws Exception {			
		/* Precondition : bob099 has the scheduled vacation on the meeting time */
		initCommand();		
		buildCommand("employee-id", "bob099");
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");
		AddVacation preVac = new AddVacation(command_array);
		preVac.execute();
		
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:40");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75"); 
		buildCommand("description", "Another meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_9() throws Exception {			
		/* Precondition : bob099 has already scheduled at the same time */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "10:30");
		buildCommand("end-time",    "12:45");
		buildCommand("room-id",     "2A07");  
		buildCommand("description", "Another meeting");
		buildCommand("attendee",    "tolas9999 bob099 gayxx067");
		AddMeeting preMeet = new AddMeeting(command_array);
		preMeet.execute();
		
		/* Test command data build */
		initCommand();		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:40");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75"); 
		buildCommand("description", "BrainStorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_10() throws Exception {			
			
		/* Test command data build */
		initCommand();		
		buildCommand("meet-date",   "12062016");
		buildCommand("start-time",  "11:40");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75"); 
		buildCommand("description", "BrainStorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}
	
	@Test(expected=AddMeetingException.class)
	public void testAddMeetingTC_F1_11() throws Exception {			
			
		/* Test command data build */
		initCommand();		
		buildCommand("meet-date",   "12062016");
		buildCommand("start-time",  "11:40");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     ""); 
		buildCommand("description", "BrainStorming meeting");
		buildCommand("attendee",    "gayxx067 bob099");		
				
		AddMeeting addmeet = new AddMeeting(command_array);
		addmeet.execute();			
	}

}
