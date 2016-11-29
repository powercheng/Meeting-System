package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import common.SysConfig;
import controller.AddMeeting;
import controller.AddVacation;
import controller.EditMeeting;
import exceptions.AddMeetingException;
import exceptions.EditMeetingException;

public class EditMeetingTest {
	
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
	public void testEditMeetingTC_F2_1() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("date",        "12072016");  // changing date 		
		EditMeeting edtmeet = new EditMeeting(command_array);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test
	public void testEditMeetingTC_F2_2() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");		
		buildCommand("start-time",  "13:30");  // changing time
		buildCommand("end-time",    "14:30");  // changing time 		
		EditMeeting edtmeet = new EditMeeting(command_array);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test
	public void testEditMeetingTC_F2_3() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("room-id",    "3D75");
		EditMeeting edtmeet = new EditMeeting(command_array);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test
	public void testEditMeetingTC_F2_4() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("description",    "Normal meeting");
		EditMeeting edtmeet = new EditMeeting(command_array);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test
	public void testEditMeetingTC_F2_5() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("attendee",   "nguy0621 smith0001");
		EditMeeting edtmeet = new EditMeeting(command_array, SysConfig.addTag);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test
	public void testEditMeetingTC_F2_6() {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		boolean noException  = true;
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		try {
			addmeet.execute();
		} catch (AddMeetingException admex) {
			noException = false;
		}		
		/* Test if change the meeting date */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("attendee",   "bob099");
		EditMeeting edtmeet = new EditMeeting(command_array, SysConfig.removeTag);
		try {
			edtmeet.execute();
		} catch (EditMeetingException edtex) {
			//edtex.printStackTrace();
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);		
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_7() throws Exception {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 1 
		
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Another meeting");
		buildCommand("attendee",    "tolas9999 desil1337");		
		
		addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 2
		
		
		/* Test if conflicts with the room's previous schedules */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("room-id",   "3D75");
		EditMeeting edtmeet = new EditMeeting(command_array);
		edtmeet.execute();
				
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_8() throws Exception {
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "bob099 gayxx067");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 1 
		
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3D75");
		buildCommand("description", "Another meeting");
		buildCommand("attendee",    "tolas9999 desil1337");		
		
		addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 2
				
		/* Test if add a new attendee and conflicts with scheduled meeting */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("attendee",   "bob099");
		EditMeeting edtmeet = new EditMeeting(command_array, SysConfig.addTag);
		edtmeet.execute();
				
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_9() throws Exception {
		
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 1 
		
		initCommand();		
		buildCommand("employee-id", "bob099");
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");
		AddVacation addVac = new AddVacation(command_array);
		addVac.execute();
		
		/* Test if adding a attendee and conflict with holiday */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("attendee",   "bob099");
		EditMeeting edtmeet = new EditMeeting(command_array, SysConfig.addTag);
		edtmeet.execute();		
				
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_10() throws Exception {
		
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 1 
		
		/* Test if adding a attendee and conflict with holiday */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("attendee",   "test001");
		EditMeeting edtmeet = new EditMeeting(command_array, SysConfig.addTag);
		edtmeet.execute();						
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_11() throws Exception {
		
		/* Test command data build */
		initCommand();		
		/* precondition : save testable data */		
		buildCommand("date",        "12062016");
		buildCommand("start-time",  "11:30");
		buildCommand("end-time",    "12:30");
		buildCommand("room-id",     "3A66");
		buildCommand("description", "Brainstorming meeting");
		buildCommand("attendee",    "gayxx067");		
		
		AddMeeting addmeet = new AddMeeting(command_array);		
		addmeet.execute();  // pre insert 1 
		
		/* Test if no such room id in the database */
		initCommand();	
		buildCommand("meeting-id", "1");
		buildCommand("room-id",    "3Z99");
		EditMeeting edtmeet = new EditMeeting(command_array);
		edtmeet.execute();						
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_12() throws Exception {
		/* Test if no such a meeting id in the database */
		initCommand();	
		buildCommand("meeting-id", "10");
		buildCommand("room-id",    "3A66");
		EditMeeting edtmeet = new EditMeeting(command_array);
		edtmeet.execute();						
	}
	
	@Test (expected=EditMeetingException.class)
	public void testEditMeetingTC_F2_13() throws Exception {
		/* Test if no such a meeting id in the database */
		initCommand();	
		buildCommand("meet-id",    "1");
		buildCommand("room-id",    "3A66");
		EditMeeting edtmeet = new EditMeeting(command_array);
		edtmeet.execute();						
	}

}
