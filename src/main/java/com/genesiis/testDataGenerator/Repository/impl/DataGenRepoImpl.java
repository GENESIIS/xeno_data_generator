/*
 * 20191209 NJ XENO-94 init and added code to extract table meta data
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation and we-write a method to extract meta suing jdbc template
 * 20200106 NJ XENO-94 - changed a variable name of the gtFkTbleMetaDta(..)
 * 20200106 NJ XENO-94 - removed unused code and added loggers and exception handling
 * 20200128 RP XENO-94 - commenting
 * */

/**
 * @author nipuna
 *
 */
package com.genesiis.testDataGenerator.Repository.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.dto.DbMetaData;
import com.genesiis.testDataGenerator.dto.MetaData;

@Repository
public class DataGenRepoImpl implements DataGenRepo {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DbMetaData dbMData;
	@Autowired
	private MetaData columnMeta;
	private static final Logger logger = LogManager.getLogger(DataGenRepoImpl.class);

	/**
	 * Get the table meta details (Column names, column types, null able etc ...) the user has entered
	 * @param tableName - Name of table entered by the user
	 */
	@Override
	public List<MetaData> getTbleMetaData(String tbleName) {

		String query = "Select * from xeno." + tbleName + ""; //Get all record from the table

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<MetaData>>() {

			@Override
			public List<MetaData> extractData(ResultSet rs) throws SQLException {
				ArrayList<MetaData> metaDataList = new ArrayList<>();//variable for the table meta data list

				try {

					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

						columnMeta = new MetaData();
						columnMeta.setColumnName(rsmd.getColumnName(i));//column name 
						columnMeta.setColumnTypeName(rsmd.getColumnTypeName(i));//column type
						columnMeta.setColumnSize(rsmd.getColumnDisplaySize(i));//column size
						columnMeta.setAutoIncrement(Boolean.toString(rsmd.isAutoIncrement(i)));//auto increment or not
						columnMeta.setIsNullable(rsmd.isNullable(i));//null able or not
						columnMeta.setPrecision(rsmd.getPrecision(i));//number column precision
						columnMeta.setScale(rsmd.getScale(i));//number column scale

						metaDataList.add(columnMeta);

					}
				} catch (SQLException e) {
					logger.error("getTbleMetaData(..) : SQL Exception :", e);
				} catch (Exception e) {
					logger.error("getTbleMetaData(..) : ", e);
				}

				return metaDataList;//return column meta data list 
			}
		});

	}

	/**
	 * Get the table meta details (Column names, column types, null able etc ...)
	 * @param tbleName - Table name that wants to get the table meta details
	 * @return List<MetaData> - list of MetaData
	 */
	public List<MetaData> gtFkTbleMetaDta(String tbleName) {

		String query = "Select * from xeno." + tbleName + "";//select all values from the table

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<MetaData>>() {

			@Override
			public List<MetaData> extractData(ResultSet rs) throws SQLException { // get the values as a List of MetaData objects

				ArrayList<MetaData> metaDataList = new ArrayList<>();
				try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {

					DatabaseMetaData dbm = conn.getMetaData();

					ResultSet rss = dbm.getColumns(null, "XENO", tbleName, null);

					while (rss.next()) {
						columnMeta = new MetaData();
						columnMeta.setColumnName(rss.getString("COLUMN_NAME"));//get the column name
						columnMeta.setColumnTypeName(rss.getString("TYPE_NAME"));// get the column type
						columnMeta.setColumnSize(rss.getInt("COLUMN_SIZE"));//get the column size
						columnMeta.setAutoIncrement(rss.getString("IS_AUTOINCREMENT"));//check whether column is auto increment or not
						columnMeta.setIsNullable(rss.getInt("NULLABLE"));//check whether column is null able
						metaDataList.add(columnMeta);
					}

				} catch (Exception e) {

					logger.error("gtFkTbleMetaDta() : Exception : ", e);
				}

				return metaDataList;
			}
		});

	}

	/**
	 * Repository class for the insert record to the table
	 * @param args - Array of Column name string and values list string 
	 * @param tableName- The Name of the table where want to insert the data
	 */
	@Override
	public void insrtTextData(Object[] args, String tableName) {

		try {
			
			String vals = args[1].toString();//get value list string
			String query = "INSERT INTO xeno." + tableName + "(" + args[0].toString() + ")VALUES" + vals + "";//Insert string query

			jdbcTemplate.batchUpdate(query);//execute the insert statement

		} catch (Exception e) {
			logger.error("insrtTextData() : ", e);
		}

	}
	
	/**
	 * Repository class for the insert record to the table
	 * @param queyParams - Array of Column name string and values list string 
	 * @param tableName- The Name of the table where you want to insert the data
	 */
	@Override
	public void insrtData(String[] queryParams, String tableName) {

		try {
			//Create insert statement using the parsed values
			String query = "INSERT INTO xeno." + tableName + "(" + queryParams[0] + ")VALUES" + queryParams[1] + "";

			jdbcTemplate.batchUpdate(query);//Insert the records

		} catch (Exception e) {
			logger.error("insrtData() : ", e);
		}

	}

	/**
	 * Repository method for the getting table's foreign keys
	 * @param tableName - Table name to retrieve keyword meta information
	 */
	public List<DbMetaData> getKeys(String tableName) throws SQLException {

		ArrayList<DbMetaData> dbMetaData = new ArrayList<>();//variable for the store table meta data
		
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				Connection con1 = jdbcTemplate.getDataSource().getConnection()) {

			
			DatabaseMetaData dbm = con.getMetaData();
			DatabaseMetaData dbm1 = con1.getMetaData();
			//fetch all the Foreign Key Columns and FK reference table related to table
			ResultSet rs = dbm.getImportedKeys(null, "XENO", tableName);

			while (rs.next()) {

				dbMData = new DbMetaData();

				dbMData.setFkParentTable(rs.getString("FKCOLUMN_NAME"));//get the FK column name
				dbMData.setFkParentTblName(rs.getString("PKTABLE_NAME"));//get the primary key table name
				
				//fetch the primary keys of FK reference table
				ResultSet rs1 = dbm1.getPrimaryKeys(null, "XENO", dbMData.getFkParentTblName());

				while (rs1.next()) {
					dbMData.setPrimaryKey(rs1.getString("COLUMN_NAME"));//get the FK reference table's primary key column name
					dbMData.setPrimaryKTble(rs1.getString("TABLE_NAME"));//get the FK reference table's table name

				}

				dbMetaData.add(dbMData);//add fetched FK and PK to dbMetaData list
			}
		} catch (Exception e) {
			logger.error("getKeys(..) : ", e);
		}
		return dbMetaData;//return the dbMetaData list
	}

	@Override
	public List<MetaData> retColumnData(List<DbMetaData> dbMeta) throws SQLException {

		ArrayList<MetaData> tableMeta = new ArrayList<>();

		try (Connection con = jdbcTemplate.getDataSource().getConnection()) {

			String tableName = dbMData.getFkParentTblName();

			DatabaseMetaData dbm = con.getMetaData();
			ResultSet rs = dbm.getColumns(null, "XENO", tableName, null);

			while (rs.next()) {
				columnMeta = new MetaData();
				columnMeta.setColumnName(rs.getString("COLUMN_NAME"));
				columnMeta.setColumnTypeName(rs.getString("TYPE_NAME"));
				columnMeta.setColumnSize(rs.getInt("COLUMN_SIZE"));
				columnMeta.setAutoIncrement(rs.getString("IS_AUTOINCREMENT"));
				columnMeta.setIsNullable(rs.getInt("NULLABLE"));
				tableMeta.add(columnMeta);
			}

		} catch (Exception e) {
			logger.error("retColumnData(..) : ", e);
		}
		return tableMeta;
	}

	/**
	 * Get the inserted column values of the table
	 * @param tableName - Table name that is want to get the records
	 * @param columnName - column name
	 */
	@Override
	public ArrayList<Object> insertFkTbleDta(String tableName, String columnName) {

		ArrayList<Object> rows = new ArrayList<>();

		String query = "Select " + columnName + " from xeno." + tableName + ""; //select the all column records of the mention table

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<ArrayList<Object>>() {

			@Override
			public ArrayList<Object> extractData(ResultSet rs) throws SQLException {

				try (Connection con = jdbcTemplate.getDataSource().getConnection();
						Statement stmt = con.createStatement();
						ResultSet rss = stmt.executeQuery(query);) {

					while (rss.next()) {//loop the fetched records

						rows.add(rss.getObject(columnName));// add inserted column values to the array list

					}

				} catch (SQLException e) {

					logger.error("insertFkTbleDta(..) : ", e);
				}

				return rows;//return array list object
			}
		});

	}

}
