package controller;

import model.Meeting;
import model.Room;
import model.Sql;
import model.Vacation;
import View.Messageout;

public class PrintScheduleRoom extends Command {
	private Room room;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Sql sql = new Sql();

		sql.read();
		sql.write();
		viewprint();
		
	}

}
