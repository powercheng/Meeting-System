package controller;

import model.Meeting;
import View.Messageout;

public class EditMeeting extends Command {
	private Meeting meeting;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.checkValid();

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
