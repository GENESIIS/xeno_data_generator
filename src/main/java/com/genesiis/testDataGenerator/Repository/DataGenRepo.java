/**
 * @author nipuna
 *20191216 NJ XENO-97 - init 
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
	
	public List<Object> getTbleMetaData();
	
	public void insrtData(String [] queryParams,String tableName);
	
	public void insrtTextData(Object [] queryParams,String tableName);
	
	public List<DbMetaData> getKeys() throws SQLException;
	
	public List<MetaData> retColumnData(ArrayList<DbMetaData> dbMeta) throws SQLException;
	
	public List<MetaData> getTbleMetaData(String tableName);

	
}