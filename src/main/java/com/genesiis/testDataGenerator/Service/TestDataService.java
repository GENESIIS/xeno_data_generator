/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  20200106 NJ XEN0-94 - changed the exception type of executeDataInsert to Exception 
 *  */
package com.genesiis.testDataGenerator.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.genesiis.testDataGenerator.dto.DbMetaData;

@Service
public interface TestDataService {

	public List<Object> generateTstData(int numOfLoops, String tableName);

	public Object[] crtQueryStrng(ArrayList params, ArrayList values);

	public Object[] crtBlkQryStrng(ArrayList params, ArrayList values);

	public void executeDataInsert(String numOfLoops, String tableName);

	public Map<String, String> getColumnData(String mainTable);

	public List<DbMetaData> getForiegnKeys(String tableName) throws SQLException;

	public String[] getFKeyMeta(String mainTable);

	public void mainExecutor(String numOfLoops, String mainTable) ;

}
