package controller;

import View.Messageout;
import model.Meeting;


public class AddMeeting extends Command {
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
