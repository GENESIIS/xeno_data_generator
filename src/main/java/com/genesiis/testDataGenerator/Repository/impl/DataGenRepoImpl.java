/*
 * 20191209 NJ XENO-94 init and added code to extract table meta data
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation and we-write a method to extract meta suing jdbc template
 * 20200106 NJ XENO-94 - changed a variable name of the gtFkTbleMetaDta(..)
 * 20200106 NJ XENO-94 - removed unused code and added loggers and exception handling
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

	@Override
	public List<MetaData> getTbleMetaData(String tbleName) {

		String query = "Select * from xeno." + tbleName + "";

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<MetaData>>() {

			@Override
			public List<MetaData> extractData(ResultSet rs) throws SQLException {
				ArrayList<MetaData> metaDataList = new ArrayList<>();

				try {

					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

						columnMeta = new MetaData();
						columnMeta.setColumnName(rsmd.getColumnName(i));
						columnMeta.setColumnTypeName(rsmd.getColumnTypeName(i));
						columnMeta.setColumnSize(rsmd.getColumnDisplaySize(i));
						columnMeta.setAutoIncrement(Boolean.toString(rsmd.isAutoIncrement(i)));
						columnMeta.setIsNullable(rsmd.isNullable(i));
						columnMeta.setPrecision(rsmd.getPrecision(i));
						columnMeta.setScale(rsmd.getScale(i));

						metaDataList.add(columnMeta);

					}
				} catch (SQLException e) {
					logger.error("getTbleMetaData(..) : SQL Exception :", e);
				} catch (Exception e) {
					logger.error("getTbleMetaData(..) : ", e);
				}

				return metaDataList;
			}
		});

	}

	public List<MetaData> gtFkTbleMetaDta(String tbleName) {

		String query = "Select * from xeno." + tbleName + "";

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<MetaData>>() {

			@Override
			public List<MetaData> extractData(ResultSet rs) throws SQLException {

				ArrayList<MetaData> metaDataList = new ArrayList<>();
				try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {

					DatabaseMetaData dbm = conn.getMetaData();

					ResultSet rss = dbm.getColumns(null, "XENO", tbleName, null);

					while (rss.next()) {
						columnMeta = new MetaData();
						columnMeta.setColumnName(rss.getString("COLUMN_NAME"));
						columnMeta.setColumnTypeName(rss.getString("TYPE_NAME"));
						columnMeta.setColumnSize(rss.getInt("COLUMN_SIZE"));
						columnMeta.setAutoIncrement(rss.getString("IS_AUTOINCREMENT"));
						columnMeta.setIsNullable(rss.getInt("NULLABLE"));
						metaDataList.add(columnMeta);
					}

				} catch (SQLException e) {

					logger.error("gtFkTbleMetaDta() : SQL Exception : ", e);
				} catch (Exception e) {

					logger.error("gtFkTbleMetaDta() : SQL Exception : ", e);
				}

				return metaDataList;
			}
		});

	}

	@Override
	public void insrtTextData(Object[] args, String tableName) {

		try {

			String vals = args[1].toString();
			String query = "INSERT INTO xeno." + tableName + "(" + args[0].toString() + ")VALUES" + vals + "";

			jdbcTemplate.batchUpdate(query);

		} catch (Exception e) {
			logger.error("insrtTextData() : ", e);
		}

	}

	@Override
	public void insrtData(String[] queryParams, String tableName) {

		try {

			String query = "INSERT INTO xeno." + tableName + "(" + queryParams[0] + ")VALUES" + queryParams[1] + "";

			jdbcTemplate.batchUpdate(query);

		} catch (Exception e) {
			logger.error("insrtData() : ", e);
		}

	}

	public List<DbMetaData> getKeys(String tableName) throws SQLException {

		ArrayList<DbMetaData> dbMetaData = new ArrayList<>();

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				Connection con1 = jdbcTemplate.getDataSource().getConnection()) {

			DatabaseMetaData dbm = con.getMetaData();
			DatabaseMetaData dbm1 = con1.getMetaData();
			ResultSet rs = dbm.getImportedKeys(null, "XENO", tableName);

			while (rs.next()) {

				dbMData = new DbMetaData();

				dbMData.setFkParentTable(rs.getString("FKCOLUMN_NAME"));
				dbMData.setFkParentTblName(rs.getString("PKTABLE_NAME"));

				ResultSet rs1 = dbm1.getPrimaryKeys(null, "XENO", dbMData.getFkParentTblName());

				while (rs1.next()) {
					dbMData.setPrimaryKey(rs1.getString("COLUMN_NAME"));
					dbMData.setPrimaryKTble(rs1.getString("TABLE_NAME"));

				}

				dbMetaData.add(dbMData);
			}
		} catch (Exception e) {
			logger.error("getKeys(..) : ", e);
		}
		return dbMetaData;
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

	@Override
	public ArrayList<Object> insertFkTbleDta(String tableName, String columnName) {

		ArrayList<Object> rows = new ArrayList<>();

		String query = "Select " + columnName + " from xeno." + tableName + "";

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<ArrayList<Object>>() {

			@Override
			public ArrayList<Object> extractData(ResultSet rs) throws SQLException {

				try (Connection con = jdbcTemplate.getDataSource().getConnection();
						Statement stmt = con.createStatement();
						ResultSet rss = stmt.executeQuery(query);) {

					while (rss.next()) {

						rows.add(rss.getObject(columnName));

					}

				} catch (SQLException e) {

					logger.error("insertFkTbleDta(..) : ", e);
				}

				return rows;
			}
		});

	}

}
