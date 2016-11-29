package mainFeature;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import controller.AddHoliday;
import exceptions.AddHolidayException;

public class AddHolidayTest {

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
	public void testAddHolidayTC_F9_1() {
		/* test if add holiday successfully */
		initCommand();	
		buildCommand("start-date",  "12232016");
		buildCommand("end-date",    "12262016");
		buildCommand("description", "Christmas holiday");
			
		AddHoliday addholi = new AddHoliday(command_array);
		boolean noException  = true;
		
		try {
			addholi.execute();
		} catch (AddHolidayException avex) {
			noException = false;
		}
		
		assertTrue(noException);
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_2() throws Exception {
		/* Test if start date is out of range */
		initCommand();		
		buildCommand("start-date",  "12332016");  // invalid
		buildCommand("end-date",    "12262016");
		buildCommand("description", "Christmas holiday");
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_3() throws Exception {
		/* Test if start date is empty */
		initCommand();		
		buildCommand("start-date",  "");  // empty
		buildCommand("end-date",    "12262016");
		buildCommand("description", "Christmas holiday");
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_4() throws Exception {
		/* Test if end date is out of range */
		initCommand();		
		buildCommand("start-date",  "12232016");  
		buildCommand("end-date",    "13262016"); // invalid
		buildCommand("description", "Christmas holiday");
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_5() throws Exception {
		/* Test if end date is empty */
		initCommand();		
		buildCommand("start-date",  "12232016");  
		buildCommand("end-date",    "");// empty
		buildCommand("description", "Christmas holiday");
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_6() throws Exception {
		/* Test if description is omitted */
		initCommand();		
		buildCommand("start-date",  "12232016");  
		buildCommand("end-date",    "12262016");
		//buildCommand("description", ""); // empty
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}
	
	@Test (expected=AddHolidayException.class)
	public void testAddHolidayTC_F9_7() throws Exception {
		/* Test if invalid classifer name */
		initCommand();		
		buildCommand("start-day",   "12232016");  // invalid
		buildCommand("end-date",    "12262016");
		buildCommand("description", "Christmas holiday"); 
		AddHoliday addholi = new AddHoliday(command_array);
		addholi.execute();						
	}

}
