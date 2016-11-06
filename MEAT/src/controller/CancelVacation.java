package controller;

import model.Vacation;
import View.Messageout;

public class CancelVacation extends Command {
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.checkValid();
		Vacation vacation = new Vacation();

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
