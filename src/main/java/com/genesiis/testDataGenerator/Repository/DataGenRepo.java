/**
 * @author nipuna
 *20191216 NJ XENO-94 - init 
 */
package com.genesiis.testDataGenerator.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface DataGenRepo{
	
	public List<Object> getTbleMetaData();
	
	public void insrtData(String [] queryParams,String tableName);
	
	public void insrtTextData(Object [] queryParams,String tableName);

	
}