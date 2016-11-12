package client;

import java.io.File;
import java.util.Scanner;

import common.CommonUtil;
import common.SysConfig;
import controller.CommandFactory;



public class Main {
	private static File file = null;
	private static Scanner in;
	
	public static void main(String[] args) {
		in = new Scanner(System.in);
		while(!processInput()){
			System.out.println("file not found");	
		}
		in.close();
		String jsonData = CommonUtil.loadJsonFile(file.getPath());
		CommandFactory factory = new CommandFactory();
		factory.run(jsonData);
		return;
	}
	
	public static boolean processInput(){		
		System.out.println("input the file name which exist in the data folder or press Q for quit");
		String input = in.nextLine();
		String filePath = SysConfig.runningDir+"\\data\\"+input+ ".json";
		file = new File(filePath);
		if(input.toLowerCase().equals("q") || file.isFile() && file.exists()){			
			return true;
		} else {			
			return false;
		}
	}
}
	
