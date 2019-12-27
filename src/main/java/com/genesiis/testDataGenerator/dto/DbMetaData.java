/**
 * 
 */
package com.genesiis.testDataGenerator.dto;

import org.springframework.stereotype.Component;

/**
 * @author Nipuna
 *
 */
@Component
public class DbMetaData {
	
	private String columnName;
	private String tableName;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	

}
