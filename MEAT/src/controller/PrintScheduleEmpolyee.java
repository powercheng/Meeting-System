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
		Sql sql = new Sql();

		sql.read();
		sql.write();
		viewprint();
		
		
	}

}
