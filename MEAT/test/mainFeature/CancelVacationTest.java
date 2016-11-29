package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import controller.AddVacation;
import controller.CancelVacation;
import exceptions.AddVacationException;
import exceptions.CancelVacationException;

public class CancelVacationTest {

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
	public void testCancelVacationTC_F5_1() {
		/* Pre condition - add a new vacation of employee 'bob099' */
		initCommand();		
		buildCommand("employee-id", "bob099");
		buildCommand("start-date",  "12052016");
		buildCommand("end-date",    "12072016");
	
		AddVacation addVac = new AddVacation(command_array);
		boolean noException  = true;
		try {
			addVac.execute();
		} catch (AddVacationException advex) {
			noException = false;
		}
		
		/* Test if cancel the vacation of employee id (bob099) */
		initCommand();	
		buildCommand("employee-id", "bob099");		
		CancelVacation canvac = new CancelVacation(command_array);
		try {
			canvac.execute();
		} catch (CancelVacationException edtex) {			
			noException = false;
		}		
		/* must be no exception */
		assertTrue(noException);	
	}
	
	@Test (expected=CancelVacationException.class)
	public void testCancelVacationTC_F5_2() throws Exception {
		/* Test if no such employee id in the database */
		initCommand();	
		buildCommand("employee-id", "test001"); // invalid		
		CancelVacation canvac = new CancelVacation(command_array);
		canvac.execute();						
	}
	
	@Test (expected=CancelVacationException.class)
	public void testCancelVacationTC_F5_3() throws Exception {
		/* Test if no such classifier name */
		initCommand();	
		buildCommand("emp-id", "bob099"); // invalid		
		CancelVacation canvac = new CancelVacation(command_array);
		canvac.execute();						
	}
	
	@Test (expected=CancelVacationException.class)
	public void testCancelVacationTC_F5_4() throws Exception {
		/* Test if empty input */
		initCommand();	
		// command empty //
		CancelVacation canvac = new CancelVacation(command_array);
		canvac.execute();						
	}

}
