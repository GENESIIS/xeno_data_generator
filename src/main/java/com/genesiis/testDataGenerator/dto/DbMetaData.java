/**
 * 
 */
package com.genesiis.testDataGenerator.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nipuna
 * 20200127 RP XENO-94 - commenting
 */
@Component
@Scope("prototype")
public class DbMetaData {
	
	private String fkParentTable;//fk column name of the table
	private String fkParentTblName;//fk reference table name 
	private String primaryKey;//pk column name of the fk reference table
	private String primaryKTble;//pk table name of the fk reference table
	
	
	
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getPrimaryKTble() {
		return primaryKTble;
	}
	public void setPrimaryKTble(String primaryKTble) {
		this.primaryKTble = primaryKTble;
	}
	public String getFkParentTable() {
		return fkParentTable;
	}
	public void setFkParentTable(String fkParentTable) {
		this.fkParentTable = fkParentTable;
	}
	public String getFkParentTblName() {
		return fkParentTblName;
	}
	public void setFkParentTblName(String fkParentTblName) {
		this.fkParentTblName = fkParentTblName;
	}
	
	

}
