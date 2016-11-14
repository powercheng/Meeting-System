package controller;

import model.Vacation;
import View.Messageout;
import common.SysConfig;

public class AddHoliday extends Command {
	private Vacation[] vacations;
	
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		
		//viewprint();
		return SysConfig.success;
		
	}

}
