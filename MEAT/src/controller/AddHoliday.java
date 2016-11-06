package controller;

import model.Vacation;
import View.Messageout;

public class AddHoliday extends Command {
	private Vacation[] vacations;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.checkValid();
		
		viewprint();
		
		
	}

	@Override
	public void checkValid() {
		// TODO Auto-generated method stub
		checkDateValid();
		checkTimeValid();
		checkRoomIdValid();
		checkEmpolyeeIdValid();
		checkStrLenValid();
	}
}
