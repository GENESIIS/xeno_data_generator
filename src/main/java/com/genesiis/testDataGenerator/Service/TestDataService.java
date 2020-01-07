/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  */
package com.genesiis.testDataGenerator.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TestDataService {

	public List<Object> generateTstData(int numOfLoops) throws Exception;

	public Object[] crtQueryStrng(ArrayList params, ArrayList values);

	public Object[] crtBlkQryStrng(ArrayList params, ArrayList values);

	public void executeDataInsert(String numOfLoops, String tableName);

	public Map<String, String> getColumnData();

}
