/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  */
package com.genesiis.testDataGenerator.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TestDataService {
	
	public List<Object> generateTstData() throws Exception;
	
	public String[] crtQueryStrng(ArrayList params,ArrayList values);
	
	public void executeDataInsert();
	
	public HashMap<String, String> getColumnData();

}
