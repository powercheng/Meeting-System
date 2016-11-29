package mainFeature;

import static org.junit.Assert.*;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.CommonUtil;
import common.SysConfig;
import model.Sql;

public class InitDBTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitDBTestTC_F13_1() {
		
		CommonUtil.initDB();
		
		String meetQuery = "SELECT count(*) CNT FROM TB_MEETING";
		String attQuery = "SELECT count(*) CNT FROM TB_ATTENDEE";
		String vacQuery = "SELECT count(*) CNT FROM TB_VACATION";
		String holiQuery = "SELECT count(*) CNT FROM TB_HOLIDAY";
		
		int totalCount = 0;
		
		Sql db = new Sql();
		db.setQuery(meetQuery);
		JSONArray jarr = db.read();
		JSONObject jcnt = (JSONObject) jarr.get(0);
		totalCount += Integer.parseInt((String) jcnt.get("CNT"));
		
		db.setQuery(attQuery);
		jarr = db.read();
		jcnt = (JSONObject) jarr.get(0);
		totalCount += Integer.parseInt((String) jcnt.get("CNT"));		
		
		db.setQuery(vacQuery);
		jarr = db.read();
		jcnt = (JSONObject) jarr.get(0);
		totalCount += Integer.parseInt((String) jcnt.get("CNT"));
		
		db.setQuery(holiQuery);
		jarr = db.read();
		jcnt = (JSONObject) jarr.get(0);
		totalCount += Integer.parseInt((String) jcnt.get("CNT"));
		
		db.close();
		
		assertEquals(totalCount, 0);
		
	}
	
	@Test
	public void testInitDBTestTC_F13_2() {
		
		CommonUtil.initDB();
		
		String empQuery = "SELECT count(*) CNT FROM TB_EMPLOYEE";
		
		Sql db = new Sql();
		db.setQuery(empQuery);
		JSONArray jarr = db.read();
		db.close();
		
		JSONObject jcnt = (JSONObject) jarr.get(0);
		// restore org db file
		int fetchCount = Integer.parseInt((String) jcnt.get("CNT"));
		boolean moreThenOne = fetchCount > 0 ? true : false; 
		assertTrue(moreThenOne);		
		
	}
	
	@Test
	public void testInitDBTestTC_F13_3() {
		
		String orgdbFile = SysConfig.runningDir + "\\resource\\meat.db";
		String testdbFile = SysConfig.runningDir + "\\resource\\data.db"; // not exist
		SysConfig.dbFile = testdbFile;
		
		Sql db = new Sql();
		
		Boolean isFault = db.isFault;		
		db.close();
		// restore org db file
		SysConfig.dbFile = orgdbFile;
		
		File tfile = new File(testdbFile);
		if (tfile.exists()) tfile.delete();
				
		assertTrue(isFault);		
		
	}
	
	@Test
	public void testInitDBTestTC_F13_4() {
		
		Sql db = new Sql();
		db.read();		
		boolean isFalut = db.isFault;
		db.close();
		
		assertTrue(isFalut);		
		
	}
	
	@Test
	public void testInitDBTestTC_F13_5() {
		
		Sql db = new Sql();		
		db.write();		
		boolean isFalut = db.isFault;
		db.close();
		
		assertTrue(isFalut);		
		
	}
	
	@Test
	public void testInitDBTestTC_F13_6() {
		
		String udtQuery = "UPDATE TB_ROOM SET floor = ? where roomID = ? ";
		
		Sql db = new Sql();		
		db.setQuery(udtQuery);
		db.setParameter(1, 2); 
		db.setParameter(2, "3A66");
		db.write();		

		db.setQuery("SELECT floor FROM TB_ROOM WHERE roomID = ?");
		db.setParameter(1, "3A66");
		JSONArray jarr = db.read();
		JSONObject jobj = (JSONObject) jarr.get(0);
		String sfloor = (String) jobj.get("floor");
		int floor = Integer.parseInt(sfloor);
		db.close();
		
		assertEquals(2, floor);		
		
	}
	/**
	 * finally, clean output file and so on.
	 */
	@Test
	public void testCleanTestTempFile() {
		
		String tmp_file = SysConfig.runningDir + "\\test.json";
		String tmp_file1 = SysConfig.runningDir + "\\test1.json";
		String tmp_file2 = SysConfig.runningDir + "\\test2.json";
		String tmp_file3 = SysConfig.runningDir + "\\test3.json";
		String tmp_file4 = SysConfig.runningDir + "\\test4.json";
		File f = new File(tmp_file);
		File f1 = new File(tmp_file1);
		File f2 = new File(tmp_file2);
		File f3 = new File(tmp_file3);
		File f4 = new File(tmp_file4);
		
		if (f.exists()) f.delete();
		if (f1.exists()) f1.delete();
		if (f2.exists()) f2.delete();
		if (f3.exists()) f3.delete();
		if (f4.exists()) f4.delete();
			
		assertEquals(1, 1);		
		
	}
	
	
	
	
}
