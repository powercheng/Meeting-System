package client;

import java.util.ArrayList;

import common.CommonUtil;
import common.SysConfig;
import controller.CommandFactory;
/**
 * Main controller in our MEAT system <br>
 * How to run this program. <br>
 * 1. Interactive       mode : java -jar MEAT.jar  <br>
 * 2. ScriptCommand run mode : java -jar MEAT.jar filename.json <br>
 * 3. Load externalDB mode   : java -jar MEAT.jar DBSYNC <br>
 * @author group7
 */
public class Main {		
	/**
     * MEAT System begins !    
     * @param args 1. nothing or 2. filename or 3. DBSYNC (only input DBSYNC)
     */
	public static String runMode;
	public static int sleepMilliTime = 3000;
	public static boolean isTest = false;
	public static ArrayList<Boolean> scriptCmdStatus;
	
    public static void main(String[] args) {
    	
	    System.out.println("************* How to run the MEAT ! ****************");	   
	    System.out.println("1. Interactive     mode : java -jar MEAT.jar ");
	    System.out.println("2. ScriptCommdnRun mode : java -jar MEAT.jar filename.json ");
	    System.out.println("3. Load externalDB mode : java -jar MEAT.jar DBSYNC ");
	    System.out.println("4. Init local DB   mode : java -jar MEAT.jar DBINIT ");
	    System.out.println("\nYour commands will be running in 3second................\n");
	    
	    try {
			Thread.sleep(sleepMilliTime);
			System.out.println("Checking Resources.....");	
			/*db file check */
			if (!SysConfig.checkResource()) {
				System.out.println("System stopped....");
				if (!isTest) System.exit(0);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (!isTest) System.exit(0);
		}
	    
	    /* running mode checking */
	    if (args.length == 0) {
	    	// no passing parameter means interactive mode
	    	InteractiveMode.printMainMenu();
	    	runMode = "INTERACTIVE";
	    } else {
	    	switch (args[0].toUpperCase()) {
	    		// External DB (employee.json and room.json in resource dir) loaded into local database
		    	case "DBSYNC":   
					/* TO DO */
		    		runMode = "DBSYNC";		    		
		    		System.out.println("\n----- External DB syncronization is starting..-----\n");
		    		ExternalDBImporter dImp = new ExternalDBImporter();
		    		dImp.updateEmployeeTable(); // TB_EMPLOYEE update
		    		dImp.updateRoomTable(); // TB_ROOM update
		    		System.out.println("\n----- External DB syncronization ended..-----");
		    		break;	
		    	// All other arguments are taken as command script file. args = filename
		    	case "DBINIT":
		    		runMode = "DBINIT";		    		
		    		System.out.println("\n----- DB init is starting..-----\n");
		    		CommonUtil.initDB();
		    		System.out.println("\n----- DB init is ended..-----");
		    		break;
				default:					
					boolean isSuccess = runScriptCommand(args[0]);
					if (isSuccess) runMode = "SCRIPT";
					else runMode = null;
					break;
			}
	    }
	    if (!isTest) System.exit(0);
	}      
   /**
    * Run all commands in the script file (json format) at once 
    * Script file should be exists in our current working directory
    * SysConfig.ScriptFilePath = workingDir//
    * @param fileName  (such as command.json)
    */
   public static boolean runScriptCommand(String fileName) {	
	    System.out.println("\n----- ScriptFile running mode is starting..-----\n");
		String filePath = SysConfig.ScriptFilePath + fileName;
		String jsonData = CommonUtil.loadJsonFile(filePath);
		if (!jsonData.equals("")) {
			CommandFactory factory = new CommandFactory();
			factory.run(jsonData);
			scriptCmdStatus = factory.cmd_result_lst;
		} else {
			System.out.println("Error: The file("+fileName+") should be in the current directory.");
			return false;
		}
		System.out.println("\n----- ScriptFile running mode ended..-----");
		return true;
	}
}
