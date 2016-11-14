package common;

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
	
	public static String fail = "failed";
	public static String success = "success";

}
