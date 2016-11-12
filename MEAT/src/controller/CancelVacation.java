package controller;

import model.Vacation;
import View.Messageout;

public class CancelVacation extends Command {
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

		Vacation vacation = new Vacation();

		vacation.writeToSql();
		viewprint();
		
		
	}
}
