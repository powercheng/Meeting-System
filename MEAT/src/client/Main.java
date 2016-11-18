package client;

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
    public static void main(String[] args) {
    	
	    System.out.println("************* How to run the MEAT ! ****************");	   
	    System.out.println("1. Interactive     mode : java -jar MEAT.jar ");
	    System.out.println("2. ScriptCommdnRun mode : java -jar MEAT.jar filename.json ");
	    System.out.println("3. Load externalDB mode : java -jar MEAT.jar DBSYNC ");
	    System.out.println("\nYour commands will be running in 3second................\n");
	    
	    try {
			Thread.sleep(3000);
			System.out.println("Checking Resources.....");	
			/*db file check */
			if (!SysConfig.checkResource()) {
				System.out.println("System stopped....");
				System.exit(0);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    /* running mode checking */
	    if (args.length == 0) {
	    	// no passing parameter means interactive mode
	    	InteractiveMode.printMainMenu();
	    } else {
	    	switch (args[0].toUpperCase()) {
	    		// External DB (employee.json and room.json in resource dir) loaded into local database
		    	case "DBSYNC":   
					/* TO DO */
		    		System.out.println("\n----- External DB syncronization is starting..-----\n");
		    		ExternalDBImporter dImp = new ExternalDBImporter();
		    		dImp.updateEmployeeTable(); // TB_EMPLOYEE update
		    		dImp.updateRoomTable(); // TB_ROOM update
		    		System.out.println("\n----- External DB syncronization ended..-----");
		    		break;	
		    	// All other arguments are taken as command script file. args = filename
		    	case "INITDB":
		    		System.out.println("\n----- DB init is starting..-----\n");
		    		TestRun tr = new TestRun();
		    		tr.initDB();
		    		System.out.println("\n----- DB init is ended..-----");
		    		break;
				default: 
					runScriptCommand(args[0]);
					break;
			}
	    }
	}      
   /**
    * Run all commands in the script file (json format) at once 
    * Script file should be exists in our current working directory
    * SysConfig.ScriptFilePath = workingDir//
    * @param fileName  (such as command.json)
    */
   private static void runScriptCommand(String fileName) {	
	    System.out.println("\n----- ScriptFile running mode is starting..-----\n");
		String filePath = SysConfig.ScriptFilePath + fileName;
		String jsonData = CommonUtil.loadJsonFile(filePath);
		if (!jsonData.equals("")) {
			CommandFactory factory = new CommandFactory();
			factory.run(jsonData);
		} else {
			System.out.println("Error: The file("+fileName+") should be in the current directory.");
		}
		System.out.println("\n----- ScriptFile running mode ended..-----");
	}
}
