/*
 * 20191209 NJ XENO-94 init and added code to extract table meta data
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation and we-write a method to extract meta suing jdbc template
 * */

/**
 * @author nipuna
 *
 */
package com.genesiis.testDataGenerator.Repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.Service.impl.DataGenService;

@Repository
public class DataGenRepoImpl implements DataGenRepo{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;
	
	@Autowired
	private JdbcTemplate JdbcTemplate;
	
	

	@Override
	public List<Object> getTbleMetaData(){
		
		String query = "Select * from xeno.EMPPAYROLSUM";

		return  namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<Object>>() {

			@Override
			public List<Object> extractData(ResultSet rs) throws SQLException {
				ResultSetMetaData rsmd = rs.getMetaData();
				ArrayList<Object> metaDataList = new ArrayList<>();
				 for(int i =1;i<rsmd.getColumnCount()+1;i++) {
			    	  
				      ArrayList<Object> metaData = new ArrayList<>();
				      
				      metaData.add(rsmd.getColumnName(i));
				      metaData.add(rsmd.getColumnTypeName(i));
				      metaData.add(rsmd.getColumnDisplaySize(i));
				      metaData.add(rsmd.isAutoIncrement(i));
				      metaData.add(rsmd.isNullable(i));
				      metaData.add(rsmd.getPrecision(i));
				      metaData.add(rsmd.getScale(i));
				      
				      metaDataList.add(metaData);
	  
				      }
				
				return metaDataList;
			}
		});

	}
	
	
	public void insertData(String[]args) throws SQLException {
		
		  ArrayList<Object> metaDataList = new ArrayList<>();
	      String mysqlUrl = "jdbc:sqlserver://220.247.201.82:20020";
	      Connection con = DriverManager.getConnection(mysqlUrl, "nj_sdb", "AWSwp2!wa9");
	      
	      DataGenService dataGen = new DataGenService(); 
	      
	     //String args [] = dataGen.crtQueryStrng(input,data);
	      
	      Statement stmt = con.createStatement();
	      
	      String query = "INSERT INTO xeno.EMPPAYROLSUM("+args[0]+")VALUES"+args[1]+"";
	    
	      stmt.executeUpdate(query);
	   
		
	}
	@Override
	public void insrtTextData(Object[]args,String tableName) {
	
		String vals = args[1].toString();
		String query= "INSERT INTO xeno."+tableName+"("+args[0].toString()+")VALUES"+vals+"";
		//String query = "INSERT INTO xeno.EMPPAYROLSUM(CMPNO, EMPNO, EPFNO, EMPNAME, BASICSAL, NOTHRS, DOTHRS, NOTAMT, DOTAMT, NPDAYS, NPAMT, TOTALLO, TOTDEDU, PAYEE, CRTON, CRTBY, MODON, MODBY=varchar)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
		

		 JdbcTemplate.batchUpdate(query);
		
		
	}


	@Override
	public void insertStatsDirLocalSize(String[] params, ArrayList<Object> data) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
/*	@Transactional
	public void insertStatsDirLocalSize(String[]params,ArrayList<Object>data) throws Exception {
		//NamedParameterJdbcTemplate jdbcNamesTpl = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		
		
		
		List<MapSqlParameterSource> batchArgs = new ArrayList<>();

		for (Entry<Long, Long> e : dirLocalSize.entrySet()) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("node_id", e.getKey());
			parameters.addValue("local_size", e.getValue());
			batchArgs.add(parameters);
		}
		
		for(int i = 0;i<params.length;i++) {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue(params[i], data.get(i));
			batchArgs.add(parameters);
			
			
		}
		
		SqlParameterSource [] listRecords = SqlParameterSourceUtils.createBatch(data);
		
		String query = "INSERT INTO xeno.EMPPAYROLSUM("+params[0]+")VALUES(: CMPNO: EMPNO: EPFNO: EMPNAME: BASICSAL: NOTHRS: DOTHRS: NOTAMT: DOTAMT: NPDAYS: NPAMT: TOTALLO: TOTDEDU: PAYEE: CRTON: CRTBY: MODON: MODBY)";
		namedParameterJdbcTemplate.batchUpdate(query, batchArgs.toArray(new MapSqlParameterSource[params.length]));
	}*/

}
