package mainFeature;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.ExternalDBImporter;
import common.SysConfig;

public class ExternalDBimportTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDBImportTC_F13_1() {
		
		ExternalDBImporter imp = new ExternalDBImporter();
		imp.updateEmployeeTable();
		imp.updateRoomTable();
		boolean workSuccess = false;
		// if 2 works success
		if ( imp.importStatus[0] && imp.importStatus[1] ) {
			workSuccess = true;
		}
		
		assertTrue(workSuccess);
	}
	
	@Test
	public void testDBImportTC_F13_2() {
		
		String orgEmpJson = SysConfig.employeeJsonFile;
		String orgRoomJson = SysConfig.roomJsonFile;
		
		SysConfig.employeeJsonFile = SysConfig.runningDir + "\\resource\\rooms1.json";  // modify with no such file
		SysConfig.roomJsonFile = SysConfig.runningDir  + "\\resource\\employees1.json";   // modify with no such file 
				
		ExternalDBImporter imp = new ExternalDBImporter();
		imp.updateEmployeeTable();
		imp.updateRoomTable();
		boolean workSuccess = false;
		// if 2 works success
		if ( imp.importStatus[0] && imp.importStatus[1] ) {
			workSuccess = true;
		}
		// restore original config 
		SysConfig.employeeJsonFile = orgEmpJson;
		SysConfig.roomJsonFile = orgRoomJson;
		
		assertFalse(workSuccess);
	}

}
