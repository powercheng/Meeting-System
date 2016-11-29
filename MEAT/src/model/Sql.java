package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.SysConfig;
/**
 * Database connection and handling class (wrapper)
 * @author group7
 *
 */
public class Sql {
	
	private String     dbfilePath = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private String  Query = null;
	
	public boolean isFault = false;
	
	/**
	 * default constructor
	 */
	public Sql() {
		try {
			this.dbfilePath = SysConfig.dbFile;
			if (!(new File(dbfilePath)).exists() || (new File(dbfilePath)).length() == 0) {
				isFault = true;
				throw new SQLException("No db file exists");
			}
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbfilePath);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isFault = true;
			e.printStackTrace();
		}
	}
	
	/**
	 * close connection 
	 */
	public void close() {
		if (pstmt != null) try { pstmt.close(); } catch (Exception ex) {} ;
		if (conn != null)  try { conn.close(); }  catch (Exception ex) {};		
	}
	/**
	 * Set query into statement
	 * @param query
	 */
	public void setQuery(String query) {
		this.Query = query;
		try {
			this.pstmt = conn.prepareStatement(getQuery());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isFault = true;
		}
	}
	
	public String getQuery() {
		return this.Query;
	}
	/**
	 * Set parameter(string) into preparedstatement 
	 * @param idx
	 * @param param
	 */
	public void setParameter(int idx, String param) {
		try {
			this.pstmt.setString(idx, param);
		} catch (SQLException e) {			
			isFault = true;
		}		
	}
	/**
	 * Set parameter(long) into preparedstatement 
	 * @param idx
	 * @param param
	 */	
	public void setParameter(int idx, long param) {
		try {
			this.pstmt.setLong(idx, param);
		} catch (SQLException e) {
			isFault = true;
		}		
	}
	/**
	 * Execute insert, update, delete query
	 * @return
	 */
	public int write() {
		int n = -1;
		
		try {
			if (this.getQuery() == null) 
				throw new SQLException("No DML Query is setted.");			
			n = this.pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isFault = true;
		} 
		
		if (pstmt != null)
			try { pstmt.close(); } catch (SQLException e) {}
		
		return n;		
	}
	/**
	 * Fetch recordset and transform it into JSONArray format and return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray read() {
		
		JSONArray rsArray = new JSONArray();		
		ResultSet rs = null;
				
		try {			
			if (this.getQuery() == null) 
				throw new SQLException("No Select Query is setted.");
			
			rs = this.pstmt.executeQuery();
			while (rs.next()) {		
				//System.out.println(rs.getString(1));
				JSONObject  rObj  = new JSONObject();
				ResultSetMetaData rmd = rs.getMetaData();
                for ( int i=1; i<=rmd.getColumnCount(); i++ ) {
                	//System.out.println(rmd.getColumnName(i));
                	rObj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
                rsArray.add(rObj);
            }
			//System.out.println(rsArray.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isFault = true;
		} 
		
		if (rs != null)
			try { rs.close(); } catch (SQLException e) {}
		
		if (pstmt != null)
			try { pstmt.close(); } catch (SQLException e) {}
		
		return rsArray;
	}	
}
