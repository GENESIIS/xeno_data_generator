/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  */
package com.genesiis.testDataGenerator.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.genesiis.testDataGenerator.dto.DbMetaData;

@Service
public interface TestDataService {
	
	public List<Object> generateTstData(int numOfLoops) throws Exception;
	
	//public String[] crtQueryStrng(ArrayList params,ArrayList values);
	public Object[] crtQueryStrng(ArrayList params,ArrayList values);
	
	public Object[] crtBlkQryStrng(ArrayList params,ArrayList values); 
	
	public void executeDataInsert(String numOfLoops,String tableName);
	
	public HashMap<String, String> getColumnData();
	
	public ArrayList<DbMetaData> getForiegnKeys() throws SQLException;
	public void getFKeyMeta() throws SQLException;

}
