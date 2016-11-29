package mainFeature;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.Main;
import common.SysConfig;

public class MainTest {
	
	@Before
	public void setUp() throws Exception {		
		Main.isTest = true;
		Main.sleepMilliTime = 100; //0.1 sec sleep
	}

	@After
	public void tearDown() throws Exception {
		Main.isTest = false;
		Main.sleepMilliTime = 3000; //restore
	}

	@Test
	public void testMainTC_F10_1() {	
		
		/* Building arguments*/
		String args[] = {}; // empty arguments for intractive mode
		// simulating user input 0 
		ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
		System.setIn(in);
		Main.main(args);
		/* To do*/
		String runMode = Main.runMode;		
		// Reset System.in to its original
		System.setIn(System.in);			
		// Assert mode 			
		assertEquals(runMode, "INTERACTIVE");				
	}
	
	@Test
	public void testMainTC_F10_2() {
		/* Building arguments*/
		String args[] = {"sampleScript.json"}; // script running arguments
		Main.main(args);
		String runMode = Main.runMode;		
		// Assert mode 			
		assertEquals(runMode, "SCRIPT");
	}
	
	@Test
	public void testMainTC_F10_3() {
		/* Building arguments*/
		String args[] = {"DBINIT"}; 
		Main.main(args);
		String runMode = Main.runMode;		
		// Assert mode 			
		assertEquals(runMode, "DBINIT");
	}
	
	@Test
	public void testMainTC_F10_4() {
		/* Building arguments*/
		String args[] = {"DBSYNC"}; 
		Main.main(args);
		String runMode = Main.runMode;		
		// Assert mode 			
		assertEquals(runMode, "DBSYNC");
	}
	
	@Test
	public void testMainTC_F10_5() {
		/* Building arguments*/
		String args[] = {"TESTRUN"}; 
		Main.main(args);
		String runMode = Main.runMode;		
		// Assert mode 			
		assertEquals(runMode, null);
	}
	
	@Test
	public void testMainTC_F10_6() {
		/* Building arguments*/
		String orgPath = SysConfig.dbFile;
		String newPath = SysConfig.runningDir + "\\data.db";
		SysConfig.dbFile = newPath;
		boolean dbOkay = SysConfig.checkResource();
		SysConfig.dbFile = orgPath; // restore
		// Assert mode - false return			
		assertFalse(dbOkay);
	}
		
		
}
