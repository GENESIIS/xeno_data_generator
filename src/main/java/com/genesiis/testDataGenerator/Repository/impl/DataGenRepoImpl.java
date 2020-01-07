/*
 * 20191209 NJ XENO-94 init and added code to extract table meta data
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation and we-write a method to extract meta suing jdbc template
 * */

/**
 * @author nipuna
 *
 */
package com.genesiis.testDataGenerator.Repository.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

@Repository
public class DataGenRepoImpl implements DataGenRepo {

	private static final Logger logger = LogManager.getLogger(DataGenRepoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Object> getTbleMetaData() {

		String query = "Select * from xeno.EMPPAYROLSUM";

		return namedParameterJdbcTemplate.query(query, new ResultSetExtractor<List<Object>>() {

			@Override
			public List<Object> extractData(ResultSet rs) throws SQLException {

				ArrayList<Object> metaDataList = new ArrayList<>();
				try {

					ResultSetMetaData rsmd = rs.getMetaData();

					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

						ArrayList<Object> metaData = new ArrayList<>();

						metaData.add(rsmd.getColumnName(i));
						metaData.add(rsmd.getColumnTypeName(i));
						metaData.add(rsmd.getColumnDisplaySize(i));
						metaData.add(rsmd.isAutoIncrement(i));
						metaData.add(rsmd.isNullable(i));
						metaData.add(rsmd.getPrecision(i));
						metaData.add(rsmd.getScale(i));

						metaDataList.add(metaData);

					}
				} catch (Exception e) {

					logger.error("getTbleMetaData(..) : ", e);

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

			logger.error("insrtTextData(..) : ", e);

		}

	}

	@Override
	public void insrtData(String[] queryParams, String tableName) {

		try {

			String query = "INSERT INTO xeno." + tableName + "(" + queryParams[0] + ")VALUES" + queryParams[1] + "";

			jdbcTemplate.batchUpdate(query);

		} catch (Exception e) {

			logger.error("insrtData(..) : ", e);

		}

	}

}
