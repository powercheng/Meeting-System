package common;

/**
 * This class is used for system configuration such as db file path, common properties.
 * @author group7
 *
 */
public class SysConfig {
	
	public static String runningDir = System.getProperty("user.dir");
	
	public static String dbFile = runningDir + "\\data\\meat.db";
	
	public static String employeeJsonFile = runningDir + "\\data\\employees.json";
	public static String roomJsonFile = runningDir + "\\data\\rooms.json";
	

}
