/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  */
package com.genesiis.testDataGenerator.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.genesiis.testDataGenerator.dto.DbMetaData;

@Service
public interface TestDataService {
	
	public List<Object> generateTstData(int numOfLoops,String tableName) throws Exception;
	
	//public String[] crtQueryStrng(ArrayList params,ArrayList values);
	public Object[] crtQueryStrng(ArrayList params,ArrayList values);
	
	public Object[] crtBlkQryStrng(ArrayList params,ArrayList values); 
	
	public void executeDataInsert(String numOfLoops,String tableName) throws SQLException;
	
	public HashMap<String, String> getColumnData(String mainTable)throws SQLException;
	
	public ArrayList<DbMetaData> getForiegnKeys(String tableName) throws SQLException;
	
	public String[] getFKeyMeta(String mainTable) throws SQLException,Exception;
	
	public void removeFColumn();
	
	public void mainExecutor(String numOfLoops,String mainTable) throws Exception;
	
	public  Map<String, ArrayList<Object>> removeFColumn(int numOfLoops) throws Exception;

}
