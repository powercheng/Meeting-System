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
		this.checkValid();
		Sql sql = new Sql();

		sql.read();
		sql.write();
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
