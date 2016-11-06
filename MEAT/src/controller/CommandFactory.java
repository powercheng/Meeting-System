package controller;

public class CommandFactory {

	public CommandFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run(String[] str) {
		// TODO Auto-generated method stub
		String name = null;
		Command cmd = null;
		if(name.equals("add-meeting")){
			cmd = new AddMeeting();	
			
		} else if(name.equals("edit-meeting-details")) {
			cmd = new EditMeeting();
		} else if(name.equals("a")) {
			cmd = new CancelMeeting();
		} else if(name.equals("b")) {
			cmd = new AddVacation();
		} else if(name.equals("c")) {
			cmd = new CancelVacation();
		} else if(name.equals("d")) {
			cmd = new AddHoliday();
		} else if(name.equals("es")) {
			cmd = new PrintScheduleAll();
		} else if(name.equals("f")) {
			cmd = new PrintScheduleEmpolyee();
		} else if(name.equals("f")) {
			cmd = new PrintScheduleRoom();
		} else {
			
		}
		cmd.checkValid();
		cmd.execute();
	}
	
	
}
