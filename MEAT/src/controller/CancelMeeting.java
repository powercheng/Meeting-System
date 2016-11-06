package controller;

import model.Meeting;
import View.Messageout;

public class CancelMeeting extends Command {
	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.checkValid();
		Meeting meeting = new Meeting();

		meeting.writeToSql();
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
