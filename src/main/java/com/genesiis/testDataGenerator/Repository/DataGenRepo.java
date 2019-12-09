/*
 * 20191209 NJ XENO-94 init and added code to extract table meta data
 * */
package com.genesiis.testDataGenerator.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataGenRepo {
	
	public  List<Object> metaData() throws SQLException {
		
		  ArrayList<Object> metaDataList = new ArrayList<>();
	      DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
	      String mysqlUrl = "jdbc:sqlserver://220.247.201.82:20020";
	      Connection con = DriverManager.getConnection(mysqlUrl, "nj_sdb", "AWSwp2!wa9");
	 
	      
	      Statement stmt = con.createStatement();
	      
	      String query = "Select * from xeno.TESTDATA";
	    
	      ResultSet rs = stmt.executeQuery(query);
	      
	      ResultSetMetaData resultSetMetaData = rs.getMetaData();
	      
	      for(int i =1;i<resultSetMetaData.getColumnCount()+1;i++) {
	    	  
	      ArrayList<Object> metaData = new ArrayList<>();
	      
	      metaData.add(resultSetMetaData.getColumnName(i));
	      metaData.add(resultSetMetaData.getColumnTypeName(i));
	      metaData.add(resultSetMetaData.getColumnDisplaySize(i));
	      metaData.add(resultSetMetaData.isAutoIncrement(i));
	      metaData.add(resultSetMetaData.isNullable(i));
	      
	      metaDataList.add(metaData);
	    	  
	    	  
	      }
		
		return metaDataList;
		
	}

}
