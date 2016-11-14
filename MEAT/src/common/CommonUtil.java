package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Sql;

public class CommonUtil {
	
	public static String nullTrim(String s) 
	{
		if (s == null)
			return "";
		else 
			return s.trim();
	}
		
	public static String createUUID(int cutindex)
	{	
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString().replaceAll("-", "");
		if (uuidString.length() >= cutindex)
			return uuidString.substring(0, cutindex);
		else 
			return uuidString;			
	}
	
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
	
	public void saveJsonFile(String fileName, JSONObject jsonObj) {
		
		File file = new File(SysConfig.JsonOutDirectory + fileName);
		
		try {
			if (file.exists()) file.delete();  // previous one delete.
			
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObj.toJSONString());
			fw.flush();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
