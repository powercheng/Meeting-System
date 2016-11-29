package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Sql;
/**
 * Common utility
 * @author group7
 *
 */
public class CommonUtil {
	
	/**
	 * null string eliminate function 
	 * @param s
	 * @return
	 */
	public static String nullTrim(String s) 
	{
		if (s == null)
			return "";
		else 
			return s.trim();
	}
	/**
	 * To print a column value with fixed size in the screen
	 * @param str
	 * @param paddingSize
	 * @return
	 */
/*	
	public static String blankPadding(String str, int paddingSize) {
		
		StringBuffer rtn_buffer = new StringBuffer();
		if (str.length() >= paddingSize) {
			rtn_buffer.append(str);
		} else {
			rtn_buffer.append(str);
			int fixSize = paddingSize - str.length();
			for (int i=0;i<fixSize;i++) {
				rtn_buffer.append(" ");
			}
		}
		
		return rtn_buffer.toString();			
	}
*/
	/**
	 * Date format converter
	 * @param dateStr
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 */
	public static String dateFormat(String dateStr, String fromFormat, String toFormat) {
		
		DateFormat fm_from = new SimpleDateFormat(fromFormat); 
		SimpleDateFormat fm_to = new SimpleDateFormat(toFormat);
		String formattedDate = null;		
		Date srcDate;		
		try {
			srcDate = (Date) fm_from.parse(dateStr);
			formattedDate = fm_to.format(srcDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return formattedDate;		
	}
	/**
	 * get date string of adding days from current day with specific time string format 
	 * @param format
	 * @return
	 */
/*	
	public static String getAddDayStringFromNow(String format, int days) {
		
		SimpleDateFormat sdfDate = new SimpleDateFormat(format);
	    Date now = new Date();
	    Date futureDay = addDays(now, days);
	    String strDate = sdfDate.format(futureDay);
	    return strDate;
	}
*/	
	/**
	 * add days and return new day 
	 * @param date
	 * @param days
	 * @return
	 */
/*	
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
*/
	/**
	 * Get a next meeting ID (Sequential number)
	 * @return
	 */
	public static String getNextMeetID()
	{
		Sql db = new Sql();
		String infoQuery = "SELECT MAX(meetID)+1 AS SEQ FROM TB_MEETING ";
		db.setQuery(infoQuery);
		JSONArray jaar = db.read();
		
		if (jaar.size() == 0) {			
			return "1"; // First ID
		} else {
			JSONObject rsObj = (JSONObject) jaar.get(0);
			String meetID = (String) rsObj.get("SEQ");
			if (meetID == null) return "1";  // also first ID
			else return meetID;
		}
	}
	/**
	 * Load jsonfile contents in string data  
	 * @param jsonPath
	 * @return
	 */
	public static String loadJsonFile(String jsonPath) {
		
		StringBuffer rtn_buffer = new StringBuffer();
		BufferedReader br = null;
		
		try {
			String line;
			br = new BufferedReader(new FileReader(jsonPath));
			while ((line = br.readLine()) != null) {
				rtn_buffer.append(line);
				rtn_buffer.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}		
		return rtn_buffer.toString();
		
	}
	/**
	 * Save JSONObject into file 
	 * @param fileName
	 * @param obj
	 * @return
	 */
	public static boolean saveFile(String fileName, JSONObject obj) {		
		File file = new File(SysConfig.runningDir + "\\" + fileName);		
		try {
			if (file.exists()) file.delete();  // previous one delete.			
			FileWriter fw = new FileWriter(file);
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();			
			//System.out.println(" ( " + SysConfig.JsonOutDirectory + fileName + " is succssfully saved. )");			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
/*	
    public static String inputOutput(String msg) {
        System.out.println(msg);
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    String str = "";
	    try {
	    	str = br.readLine();
	    } catch (IOException e){
	        System.out.println("An error occurs when reading system input.");
	        //ainMenu();
	    }
	    return str;
    }
  */  
    /**
	 * Test input data clear
	 */
	public static void initDB() {
		
		try {
			String query = "DELETE FROM TB_ATTENDEE ";
			Sql db = new Sql();		
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_MEETING ";
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_VACATION ";
			db.setQuery(query);			
			db.write();
			query = "DELETE FROM TB_HOLIDAY ";
			db.setQuery(query);			
			db.write();
			// close connection
			db.close();  // make sure to call this method			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	 
}
