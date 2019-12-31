/**
 * 
 */
package com.genesiis.testDataGenerator.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nipuna
 *
 */
@Component
@Scope("prototype")
public class DbMetaData {
	
	private String fkParentTable;
	private String fkParentTblName;
	private String primaryKey;
	private String primaryKTble;
	
	
	
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
