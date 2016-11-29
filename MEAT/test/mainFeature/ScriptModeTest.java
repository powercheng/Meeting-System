package mainFeature;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.Main;
import common.CommonUtil;

public class ScriptModeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		CommonUtil.initDB();
	}

	@Test
	public void testScriptModeTC12_1() {
		String scriptName = "sampleScript.json";
		Boolean runResult = Main.runScriptCommand(scriptName);
		assertTrue(runResult);		
	}
	
	@Test
	public void testScriptModeTC12_2() {
		String scriptName = "sampleScriptError.json";
		Main.runScriptCommand(scriptName);
		Boolean containTrue = Main.scriptCmdStatus.contains((Boolean) true);  		
		assertFalse(containTrue);	// every command should result in false 	
	}

}
