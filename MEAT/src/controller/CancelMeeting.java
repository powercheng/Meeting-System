package controller;

import model.Meeting;
import View.Messageout;

public class CancelMeeting extends Command {
	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Meeting meeting = new Meeting();

		meeting.writeToSql();
		viewprint();
		
		
	}

}
