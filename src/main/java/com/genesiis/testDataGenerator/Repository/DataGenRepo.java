/**
 * @author nipuna
 *20191216 NJ XENO-97 - init 
 */
package com.genesiis.testDataGenerator.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface DataGenRepo{
	
	public List<Object> getTbleMetaData();
	
	public void insrtTextData(String [] queryParams);
	
	public void insertStatsDirLocalSize(String[]params,ArrayList<Object>data) throws Exception;
	
	public void insertData(String [] queryParams) throws SQLException;
	
	
}