package com.genesiis.testDataGenerator.dto;

import org.springframework.stereotype.Component;

@Component
public class MetaData {
	
	private String columnName;
	private String columnTypeName;
	private int columnSize;
	private boolean isAutoIncrement;
	private int isNullable;
	private int precision;
	private int scale;
	
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
	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	public void setAutoIncrement(boolean isAutoIncrement) {
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
