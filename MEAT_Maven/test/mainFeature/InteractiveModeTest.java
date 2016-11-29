package mainFeature;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.InteractiveMode;
import common.CommonUtil;


public class InteractiveModeTest {
	
	@Before
	public void setUp() throws Exception {
		InteractiveMode.mocked_cmd_array = null;
		InteractiveMode.mocked_std_stream = null;
		InteractiveMode.isTest = true;
	}

	@After
	public void tearDown() throws Exception {
		InteractiveMode.mocked_cmd_array = null;	
		InteractiveMode.mocked_std_stream = null;
		InteractiveMode.isTest = false;
		CommonUtil.initDB();
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray changeToJson(String[] names, String[] values) {
		JSONArray command_array = new JSONArray();
		for(int i = 0; i < names.length; i++) {
			if(values[i].equalsIgnoreCase("s")) continue;
			JSONObject jObj = new JSONObject();
			jObj.put("name", names[i]);
			jObj.put("value", values[i]);
	    	command_array.add(jObj);
		}		
		return command_array;
	}

	@Test
	public void testIneteractiveModeTC_F11_1() {
		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("1");   // add a meeting
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for add a meeting	
		String[] names = {
				"date","start-time","end-time","room-id","description","attendee"
		};
		
		String mock_input[] = {"12012016","11:30","12:30","3A66", "FIRST MEETING","bob099"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_2() {
		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("2");   // edit-meeting-details
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for editing a meeting	
		String[] names = {
				"meeting-id","date","start-time","end-time","room-id","description"
		};
		
		String mock_input[] = {"1", "12022016","12:30","13:30","3A66", "CHANGING MEETING"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_3() {
		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("3");   // add attendees
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for add an attendees	
		String[] names      = {"meeting-id","attendee"};		
		String[] mock_input = {"1", "smith0001 tolas9999"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_4() {
		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("4");   // remove an attendee
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for removing an attendee	
		String[] names      = {"meeting-id","attendee"};		
		String[] mock_input = {"1", "smith0001"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_5() {
		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("5");   // cancel a meeting
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for cancel a meeting	
		String[] names      = {"meeting-id"};		
		String[] mock_input = {"1"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_6() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("6");   // add a vacation
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for add a vacation	
		String[] names      = {"employee-id","start-date","end-date"};		
		String[] mock_input = {"bob099","12062016","12082016"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_7() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("7");   // cancel a vacation
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for cancel a vacation	
		String[] names      = {"employee-id"};		
		String[] mock_input = {"bob099"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_8() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("8");   // print-room-schedule
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for print-room-schedule	
		String[] names      = {"room-id","start-date","end-date","output-file"};		
		String[] mock_input = {"3A66","12012016","12302016","test1.json"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_9() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("9");   // print-employee-schedule
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for print-employee-schedule	
		String[] names      = {"employee-id","start-date","end-date","output-file"};		
		String[] mock_input = {"bob099","12012016","12302016","test2.json"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_10() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("10");   // print-company-schedule
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for print-company-schedule	
		String[] names      = {"start-date","end-date","output-file"};		
		String[] mock_input = {"12012016","12302016","test2.json"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}
	
	@Test
	public void testIneteractiveModeTC_F11_11() {		
		// build simulated user inputs for menu selecting
		StringBuffer menu_input_buffer = new StringBuffer();		
		menu_input_buffer.append("11");   // add company's holiday
		menu_input_buffer.append(System.getProperty("line.separator"));  // line sep for next input sequence
		
		// mocking input name and value for add company's holiday
		String[] names      = {"start-date","end-date","description"};		
		String[] mock_input = {"12232016","12252016","Christmas holiday"};
		
		// build simulated user inputs for value inputs
		StringBuffer value_input_buffer = new StringBuffer();
		for (int i=0;i<mock_input.length;i++) {
			value_input_buffer.append(mock_input[i]);
			value_input_buffer.append(System.getProperty("line.separator"));
		}
				
		InputStream savedStandardInputStream = System.in;
		/* Menu selection simulating */
		String simulatedMenuInput = menu_input_buffer.toString();					
		System.setIn(new ByteArrayInputStream(simulatedMenuInput.getBytes()));	
		
		/* Value input simulating, setting intractiveMode member variables to keep */
		String simulatedValueInput = value_input_buffer.toString();	
		InteractiveMode.mocked_std_stream = new ByteArrayInputStream(simulatedValueInput.getBytes());
		
		/* code that needs multiple user inputs */
		InteractiveMode.printMainMenu();
				
		// restore System standard input 
		System.setIn(savedStandardInputStream);			
		
		String mocked_json_str = InteractiveMode.mocked_cmd_array.toString();
		String actual_json_str = changeToJson(names, mock_input).toString();
		
		assertEquals(mocked_json_str, actual_json_str);
	}

}
