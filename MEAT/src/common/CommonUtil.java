package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

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
		String rtn;
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString().replaceAll("-", "");
		if (uuidString.length() >= cutindex)
			return uuidString.substring(0, cutindex);
		else 
			return uuidString;			
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

}
