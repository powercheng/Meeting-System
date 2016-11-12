package controller;

import model.Meeting;
import View.Messageout;

public class EditMeeting extends Command {
	private Meeting meeting;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

		meeting.writeToSql();
		viewprint();
		
		
	}
}
