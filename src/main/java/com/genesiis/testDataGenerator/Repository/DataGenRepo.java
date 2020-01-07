/**
 * @author nipuna
 *20191216 NJ XENO-94 - init 
 *20200107 NJ XENO-94 - removed unused abstract methods
 */
package com.genesiis.testDataGenerator.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.genesiis.testDataGenerator.dto.DbMetaData;
import com.genesiis.testDataGenerator.dto.MetaData;

@Repository
public interface DataGenRepo{
	
	public void insrtData(String [] queryParams,String tableName);
	
	public void insrtTextData(Object [] queryParams,String tableName);
	
	public List<DbMetaData> getKeys(String tableName) throws SQLException;
	
	public List<MetaData> retColumnData(List<DbMetaData> dbMeta) throws SQLException;
	
	public List<MetaData> getTbleMetaData(String tableName);
	
	public List<MetaData> gtFkTbleMetaDta(String tbleName);
	
	public ArrayList<Object>  insertFkTbleDta(String tableName,String columnName);

	
}