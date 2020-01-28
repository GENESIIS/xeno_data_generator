/**
 * Class to table column details column name, type, size etc... 
 * @author Nipuna
 * 20200128 RP XENO-94 - commenting
 */
package com.genesiis.testDataGenerator.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MetaData {
	
	private String columnName;//column name of the table
	private String columnTypeName;//column type
	private int columnSize;//column size
	private String isAutoIncrement;//is column auto incrementS
	private int isNullable;//is column null able
	private int precision;//number type column precision length
	private int scale;//number type column scale length
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}
	public String isAutoIncrement() {
		return isAutoIncrement;
	}
	public void setAutoIncrement(String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public int getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(int isNullable) {
		this.isNullable = isNullable;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	
	
	
}
