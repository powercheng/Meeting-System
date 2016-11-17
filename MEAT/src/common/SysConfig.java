package common;

import java.io.File;

/**
 * This class is used for system configuration such as db file path, common properties.
 * @author group7
 *
 */
public class SysConfig {
	
	public static String runningDir = System.getProperty("user.dir");
	
	public static String dbFile = runningDir + "\\resource\\meat.db";
	
	public static String employeeJsonFile = runningDir + "\\resource\\employees.json";
	
	public static String roomJsonFile = runningDir + "\\resource\\rooms.json";
	
	public static String JsonOutDirectory = runningDir + "\\resource\\";
	
	public static String ScriptFilePath = runningDir + "\\";
	
	public static String fail = "failed";
	public static String success = "success";
	/*max min time range */
	public static String maxDay = "12319999";
	public static String minDay = "01011900";
	/* function division tag */
	public static String addTag = "ADD";
	public static String removeTag = "REMOVE";
	
	/**
	 * If resource dir doesn't exit, then make the directory and check sqlite db file
	 */
	public static boolean checkResource() {
		
		File resourceDir = new File(JsonOutDirectory);
		if (!resourceDir.exists()) {
			resourceDir.mkdir();
		}
		
		File db = new File(dbFile);
		if (!db.exists()) {
			System.out.println(dbFile + " not in the resource directory");
			System.out.println("Check : "+fail+" \n");
			return false;
		}		
		System.out.println("Check : "+success+" \n");		
		return true;
	}

}
