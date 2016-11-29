package RunAll;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mainFeature.AddHolidayTest;
import mainFeature.AddMeetingTest;
import mainFeature.AddVacationTest;
import mainFeature.CancelMeetingTest;
import mainFeature.CancelVacationTest;
import mainFeature.EditMeetingTest;
import mainFeature.ExternalDBimportTest;
import mainFeature.InitDBTest;
import mainFeature.InteractiveModeTest;
import mainFeature.MainTest;
import mainFeature.PrintScheduleAllTest;
import mainFeature.PrintScheduleEmployeeTest;
import mainFeature.PrintScheduleRoomTest;
import mainFeature.ScriptModeTest;

@RunWith(Suite.class)
@SuiteClasses({
	AddMeetingTest.class,
	EditMeetingTest.class,
	CancelMeetingTest.class,
	AddVacationTest.class,
	CancelVacationTest.class,
	PrintScheduleRoomTest.class,
	PrintScheduleEmployeeTest.class,
	PrintScheduleAllTest.class,
	AddHolidayTest.class,
	MainTest.class,
	InteractiveModeTest.class,
	ScriptModeTest.class,
	ExternalDBimportTest.class,
	InitDBTest.class	
})

public class AllTests {}
