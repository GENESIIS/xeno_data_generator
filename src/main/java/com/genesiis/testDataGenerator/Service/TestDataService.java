/*
 *  20191216 NJ XENO-94 - included dependency injection and removed default object creation
 *  20200106 NJ XEN0-94 - changed the exception type of executeDataInsert to Exception 
 *  20200128 RP XENO-94 - commenting
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

	/**
	 * 
	 * Generate the test data for the table to which the user has insert the records
	 * @param numOfLoops - The number of records the user has to enter
	 * @param tableName - Name of the table entered by the user
	 * @return
	 */
	public List<Object> generateTstData(int numOfLoops, String tableName);
	
	/**
	 * Create a SQL value query string to insert the records- Column name string and Values string 
	 * @param - Column details
	 * @param - Values list need to insert
	 * @return
	 */
	public Object[] crtQueryStrng(ArrayList params, ArrayList values);

	/**
	 * Create the bulk query strings for the getting column names and value list string
	 * @param params - Column details
	 * @param values - Values list need to insert
	 * @return
	 */
	public Object[] crtBlkQryStrng(ArrayList params, ArrayList values);

	/**
	 * Insert the records in to user entered table
	 * @param numOfLoops - The number of records entered by the user
	 * @param tableName - Name of the table entered by the user
	 */
	public void executeDataInsert(String numOfLoops, String tableName);

	/**
	 * Get the table column details(column name, type, width, auto increment etc ...)
	 * @param tble - The table name that want to get column details
	 * @return
	 */
	public Map<String, String> getColumnData(String mainTable);

	/**
	 * Get the foreign keys of the table
	 * @param tableName - Name of the table entered by the user
	 * @return
	 * @throws SQLException
	 */
	public List<DbMetaData> getForiegnKeys(String tableName) throws SQLException;

	public String[] getFKeyMeta(String mainTable);
	
	/**
	 * Main executor of the system, system will start execution from here
	 * @param numOfLoops - user entered no of record count
	 * @param mainTable- user entered table name
	 */
	public void mainExecutor(String numOfLoops, String mainTable) ;

}
