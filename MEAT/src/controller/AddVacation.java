package controller;

import model.Meeting;
import model.Vacation;
import View.Messageout;

public class AddVacation extends Command {
	private Vacation vacation;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.checkValid();

		vacation.writeToSql();
		
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
