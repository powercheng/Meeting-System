package controller;

import model.Employee;
import model.Meeting;
import model.Sql;
import model.Vacation;
import View.Messageout;

public class PrintScheduleEmpolyee extends Command {
	private Employee employee;
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
