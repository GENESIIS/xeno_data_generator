/*
 * 20191209 NJ XENO-94- init - created the service class and printout meta data
 * 20191212 NJ XENO-94 - added new case to switch as decimal and removed unwanted code
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * 20200106 NJ XENO-94 - changed methods to generate and insert data to sub tables of a parent table binded by a  foreign key
 * 20200107 NJ XENO-94 - removed unused codes and fixed sonalrlint issues
 * 20200127 RP XENO-94 - commenting
 * 
 * */
package com.genesiis.testDataGenerator.Service.impl;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author nipuna
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.Service.TestDataService;
import com.genesiis.testDataGenerator.dto.DbMetaData;
import com.genesiis.testDataGenerator.dto.MetaData;

@Service
public class DataGenService implements TestDataService {

	@Autowired
	DataGenTypes dataGenTypes;
	@Autowired
	DataGenRepo gen;
	@Autowired
	FkDataGenTypes fkDataGenTypes;

	private static final Logger logger = LogManager.getLogger(DataGenService.class);

	/**
	 * Get the table column details(column name, type, width, auto increment etc ...)
	 * @param tble - The table name that want to get column details
	 */
	@Override
	public HashMap<String, String> getColumnData(String tble) {
		//Call the repository class to get the column details
		ArrayList<MetaData> metaData = (ArrayList<MetaData>) gen.getTbleMetaData(tble);

		//Map to store the column name and type
		HashMap<String, String> columnData = new LinkedHashMap<>();
		
		//Loop the column details values and store it in the map
		for (MetaData metData : metaData) {
			
			//Ignore the auto increment column types
			if (!(metData.isAutoIncrement().equals("true"))) {
				columnData.put(metData.getColumnName(), metData.getColumnTypeName());
			}
		}

		return columnData;//return column details
	}
	
	/**
	 * Generate the test data for the table to which the user has insert the records
	 * @param numOfLoops - The number of records the user has to enter
	 * @param tableName - Name of the table entered by the user
	 */
	@Override
	public List<Object> generateTstData(int numOfLoops, String tableName) {
		//Get the table column's meta details (column name. type, length etc...)
		ArrayList<MetaData> metaData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);

		ArrayList<Object> genDataList = new ArrayList<>();//Object list for column values ​​per row
		ArrayList<Object> genDataList1 = new ArrayList<>();//Object list for the all generated row list
		int j = 0;
	
		while (j < numOfLoops) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {

				MetaData metaObj = metaData.get(i);
				//checking whether the column is an automatic increment or ignore it if it is an automatic increment
				if (!(metaObj.isAutoIncrement().equals("true"))) {

					switch (metaObj.getColumnTypeName()) {//get column type

					case "int": //generate test data for the int type column types
					case "int identity":  //generate test data for the int identity column types

						data.add(dataGenTypes.getInt());
						genDataList.add(data);
						break;

					case "varchar"://generate test data for the varchar type column types

						String generatedString = dataGenTypes.genVarchar(metaObj.getColumnSize());
						data.add(generatedString);
						genDataList.add(data);
						break;

					case "datetime"://generate test data for the datetime type column types

						data.add(dataGenTypes.getDateTime());
						genDataList.add(data);
						break;

					case "date"://generate test data for the date type column types
						try {
							data.add(dataGenTypes.getDate());
							genDataList.add(data);
						} catch (Exception e) {
							logger.error("generateTstData(..) : switch case 'date' :", e);
						}
						break;

					case "char"://generate test data for the char type column types

						data.add(dataGenTypes.getChar());
						genDataList.add(data);
						break;

					case "decimal"://generate test data for the decimal type column types

						data.add(dataGenTypes.getDecimal(metaObj));
						genDataList.add(data);
						break;

					default:

					}

				}

			}
			genDataList1.add(data);//add every rows to the list
			j++;

		}

		return genDataList1;//return the list containing all test data
	}
	
	/**
	 * Create a SQL value query string to insert the records- Column name string and Values string 
	 * @param - Column details
	 * @param - Values list need to insert
	 */
	@Override
	public String[] crtQueryStrng(ArrayList params, ArrayList values) {

		StringBuilder str = new StringBuilder();
		StringBuilder str1 = new StringBuilder();
		//Loop the column list and create a column name string
		for (int i = 0; i < params.size(); i++) {

			str.append(params.get(i));
			if (i < params.size() - 1) {//Checking whether the columns are exist or not and if exist put ',' between columns
				str.append(","); 
			}
		}
		int b = 0;

		//Loop the value list and create a values string
		for (int k = 0; k < values.size(); k++) {
			str1.append("(");
			//get the object value of array list
			ArrayList<Object> arr = (ArrayList<Object>) values.get(k);
			int arrSize = arr.size();
			//loop the object's values and create a string values query
			for (int j = 0; j < arrSize; j++) {

				if (arr.get(1).toString().equals("int") || arr.get(1).toString().equals("decimal")) {//checking whether column data type is int or decimal 

					str1.append(arr.get(j));

				} else {//if column data type is not int or decimal then append the string
					str1.append("\'");//Append the varchars starting single quotation
					str1.append(arr.get(j));
					str1.append("\'");//Append the varchars ending single quotation

				}
				if (j != arr.size() - 1) {//checking the fetched object array size and put comma between array values
					str1.append(",");
				}
			}
			if (k == values.size() - 1) {//Checking the parsed array list size and put the ')' mark between values list
				str1.append(")");
			} else {
				str1.append("),");
			}
		}

		String queryParam = str.toString();
		String vals = str1.toString();

		String[] queryArgs = { queryParam, vals };//Create a string array using above two string

		return queryArgs;// return the string array
	}
	
	/**
	 * Create the bulk query strings for the getting column names and value list string
	 * @param params - Column details
	 * @param values - Values list need to insert
	 */
	@Override
	public Object[] crtBlkQryStrng(ArrayList params, ArrayList values) {

		StringBuilder str = new StringBuilder();

		ArrayList<String> valueList = new ArrayList<>();//To keep insert value list string
		
		//Create the column string for the insert query
		for (int i = 0; i < params.size(); i++) {

			str.append(params.get(i));
			if (i < params.size() - 1) {//Checking if there are column values to loop
				str.append(",");//If has put ',' end of the string
			}
		}
		int b = 0;

		for (int m = 0; m < values.size(); m++) {

			StringBuilder str1 = new StringBuilder();//to create the values string
			ArrayList<Object> arr1 = new ArrayList<>();//List for the to keep all values string
			arr1 = (ArrayList) values.get(m);

			for (int k = 0; k < arr1.size(); k++) {

				str1.append("(");//put start '(' of values string

				ArrayList<Object> arr = (ArrayList<Object>) arr1.get(k);
				int arrSize = arr.size();
				for (int j = 0; j < arrSize; j++) {

					if (arr.get(1).toString().equals("int") || arr.get(1).toString().equals("decimal")) {//checking whether column data type is int or decimal 

						str1.append(arr.get(j));

					} else {//if column data type is not int or decimal then append the string
						str1.append("\'");//Append the varchars starting single quotation
						str1.append(arr.get(j));
						str1.append("\'");//Append the varchars ending single quotation

					}
					if (j != arr.size() - 1) {//checking the fetched object array size and put comma between array values
						str1.append(",");
					}

				}
				if (k == arr1.size() - 1) {//Checking the parsed array list size and put the ')' mark between values list
					str1.append(")");
				} else {
					str1.append("),");
				}
			}
			String vals = str1.toString();
			valueList.add(vals);//add string to array list
		}

		String queryParam = str.toString();

		Object[] queryArgs = { queryParam, valueList };

		return queryArgs;//parse the string array list
	}

	/**
	 * Insert the records in to user entered table
	 * @param numOfLoops - The number of records entered by the user
	 * @param tableName - Name of the table entered by the user
	 */
	@Override
	public void executeDataInsert(String numOfLoops, String tableName) {
		try {
			ArrayList<DbMetaData> dbm = getForiegnKeys(tableName);//get the table fk related details
			ArrayList<Object> fkTblePkVals = new ArrayList<>();//To store the inserted FK column values
			ArrayList<String> parentTables = new ArrayList<>();//To store the FK reference table names

			int loops = Integer.parseInt(numOfLoops);
			HashMap<String, String> columnData = getColumnData(tableName);//get the column name and type as a map
			ArrayList<Object> genTestedData = null;

			ArrayList<MetaData> colData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);//Get the table all column details(name, type, length etc..)
			//checking whether table has fk or not
			if (dbm == null || dbm.isEmpty()) {
				//If there is no FK in the table, generate values for the table
				genTestedData = (ArrayList<Object>) generateTstData(loops, tableName);

			} else {//If has FK

				for (int i = 0; i < dbm.size(); i++) {
					DbMetaData dbmObj = dbm.get(i);
					//Get the inserted FK column values from the FK reference table 
					ArrayList<Object> fkTblePkVal = gen.insertFkTbleDta(dbmObj.getFkParentTblName(),
							dbmObj.getPrimaryKey());
					parentTables.add(dbmObj.getFkParentTable());
					fkTblePkVals.add(fkTblePkVal);

				}
				//Generate data for the 
				genTestedData = (ArrayList<Object>) genTestDtaMulti(loops, colData, fkTblePkVals, parentTables);

			}

			ArrayList<String> columnDataArr = new ArrayList<>(columnData.keySet());
			tableName = tableName.trim();//remove the space from table name
			insertBulk(genTestedData, tableName, columnDataArr);//execute the insert values statement

			logger.info("Data Generation was successfull");
			logger.info(genTestedData.size() + " rows were generated!!!!!");

		} catch (Exception e) {
			logger.error("executeDataInsert(..) : ", e);
		}

	}
	
	/**
	 * Get the foreign keys of the table
	 * @param tableName - Name of the table entered by the user
	 */
	@Override
	public ArrayList<DbMetaData> getForiegnKeys(String tableName) throws SQLException {
		return (ArrayList<DbMetaData>) gen.getKeys(tableName);//get table foreign key related details
	}

	@Override
	public String[] getFKeyMeta(String mainTable) {

		ArrayList<DbMetaData> dbMetaObj;
		ArrayList<String> columnDataArr = new ArrayList<>();
		ArrayList<Object> genTestedData = null;

		try {
			dbMetaObj = getForiegnKeys(mainTable);

			DbMetaData dbMeta = dbMetaObj.get(0);
			String tableName = dbMeta.getFkParentTblName();

			ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
			for (MetaData metaData : columnData) {

				if (!metaData.isAutoIncrement().equals("yes")) {
					columnDataArr.add(metaData.getColumnName());
				}
			}
			genTestedData = (ArrayList<Object>) generateTstData(columnDataArr.size(), mainTable);

		} catch (SQLException e) {
			logger.error("getFKeyMeta() :", e);
		}

		return crtQueryStrng(columnDataArr, genTestedData);

	}
	
	/**
	 * Generate the test data for the FK table
	 * @param numOfLoops - 
	 * @param table - Name of the table entered by the user
	 * @param dbm
	 * @return
	 */
	public Map<String, ArrayList<Object>> removeFColumn(int numOfLoops, String table, DbMetaData dbm) {

		Map<String, ArrayList<Object>> splitMap = new LinkedHashMap<>();
		ArrayList<Object> fKeyCol = new ArrayList<>();//Variable to keep the FK values
		ArrayList<Object> norCol = new ArrayList<>();//Variable to keep the other column values
		int indxOfPk = 0;

		try {
			
			//getting the FKs and FK reference table details related to the FK reference table that user entered table
			ArrayList<DbMetaData> dbMetaObj = (ArrayList<DbMetaData>) gen.getKeys(table);
			String pk = "";

			ArrayList<String> tableNames = new ArrayList<>();
			tableNames.add(dbm.getFkParentTable());//add fetch FK table name to the tableNames list

			//get the inserted primary key values of the FK table
			ArrayList<Object> fkTblePkVals = gen.insertFkTbleDta(tableNames.get(0), dbm.getPrimaryKey());

			//Get the meta table details related to the FK reference table that user entered table (Column names, column types, null able etc..)
			ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableNames.get(0));
			
			//Generate the test data according to data type in columns
			ArrayList<Object> testData = (ArrayList<Object>) genTestDtaMulti(numOfLoops, columnData, fkTblePkVals,
					tableNames);

			splitMap.put("colTstData", testData);
			for (DbMetaData dbMetaData : dbMetaObj) {
				pk = dbMetaData.getPrimaryKey();//Get the primary key column

				for (int i = 0; i < columnData.size(); i++) {
					MetaData mData = columnData.get(i);
					if (mData.getColumnName().equals(pk)) {//Check whether PK column name is equal or not
						indxOfPk = i;//get the primary key index
					}
				}

				for (int i = 0; i < testData.size(); i++) {
					ArrayList<Object> innerArr;
					innerArr = (ArrayList<Object>) testData.get(i);
					fKeyCol.add(innerArr.get(indxOfPk));//add the PK coloumn's value
					norCol.add(innerArr);//add other column values

				}

				splitMap.put("pkList", fKeyCol);//store the FK values
				splitMap.put("colTstData", testData);//store the test data 
			}

		} catch (Exception e) {
			
			logger.error("removeFColumn(..) : ", e);
		}
		return splitMap;//return the aplitMap result
	}

	/**
	 * Generate the the test data according to table columns type
	 * @param numOfLoops - User entered number of record count
	 * @param metaData - Table's meta details (column name, column type, column width etc..)
	 * @param pkList - Inserted primary keys 
	 * @param pk- Table name
	 * @return
	 */
	public List<Object> genTestDtaMulti(int numOfLoops, List<MetaData> metaData, List<Object> pkList, List<String> pk){

		ArrayList<Object> genDataList = new ArrayList<>();//Object list for column values ​​per row
		ArrayList<Object> genDataList1 = new ArrayList<>();//Object list for all row records

		int j = 0;

		while (j < numOfLoops) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {

				MetaData metaObj = metaData.get(i);
				
				//check whether column is auto increment or not
				if (!(metaObj.isAutoIncrement().equals("true"))) {
					switch (metaObj.getColumnTypeName()) {

					case "int"://Generate the test data if column type is int

						if (pk.contains(metaObj.getColumnName())) {
							int location = pk.indexOf(metaObj.getColumnName());

							data.add(fkDataGenTypes.getInt((ArrayList<Object>) pkList.get(location)));
							genDataList.add(data);
						} else {

							data.add(dataGenTypes.getInt());
							genDataList.add(data);
						}

						break;

					case "varchar"://Generate the test data if column type is varchar

						String generatedString = dataGenTypes.genVarchar(metaObj.getColumnSize());
						data.add(generatedString);
						genDataList.add(data);

						break;

					case "datetime": //Generate the test data if column type is datetime

						data.add(dataGenTypes.getDateTime());
						genDataList.add(data);

						break;

					case "date": //Generate the test data if column type is date

						try {
							data.add(dataGenTypes.getDate());
						} catch (ParseException e) {
							logger.error("genTestDtaMulti(..) : ",e);
						}
						genDataList.add(data);

						break;

					case "char": //Generate the test data if column type is char

						data.add(dataGenTypes.getChar());
						genDataList.add(data);

						break;

					case "decimal": //Generate the test data if column type is decimal

						data.add(dataGenTypes.getDecimal(metaObj));
						genDataList.add(data);

						break;
					default:

					}
				}

			}
			genDataList1.add(data);
			j++;

		}

		return genDataList1;//return the generated data list

	}

	
	/**
	 * Main executor of the system, system will start execution from here
	 * @param numOfLoops - user entered no of record count
	 * @param mainTable- user entered table name
	 */
	public void mainExecutor(String numOfLoops, String mainTable) {

		try {
			//getting the FK and reference table rated to the user entered table
			ArrayList<DbMetaData> dbMetaObj = getForiegnKeys(mainTable);
			ArrayList<Object> testData;
			
			//get the user entered record count
			int loops = Integer.parseInt(numOfLoops);
			
			//Loop the FK meta data list
			for (int i = 0; i < dbMetaObj.size(); i++) {
				ArrayList<String> colData = new ArrayList<>();

				DbMetaData arr = dbMetaObj.get(i);
				String tableName = arr.getFkParentTblName();//get the FK reference table name
				String pk = arr.getPrimaryKey();//get the FK reference table's primary key column name
				
				//get the FK reference table meta data values column name , types, auto increment, null able etc...
				ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.gtFkTbleMetaDta(tableName);

				for (MetaData metaData : columnData) {
					//Add columns data details except primary key column
					if (!metaData.getColumnName().equals(pk)) {
						colData.add(metaData.getColumnName());
					}
				}
				
				//Generate the test data for the FK reference table
				LinkedHashMap<String, ArrayList<Object>> result = (LinkedHashMap<String, ArrayList<Object>>) removeFColumn(
						loops, tableName, arr);
				//get the return generated test data and parse the values to insertion process
				testData = result.get("colTstData");
				//Insert the record into FK reference table
				insertBulk(testData, tableName, colData);
			}
			//Insert the records into user entered table
			executeDataInsert(numOfLoops, mainTable);

		} catch (Exception e) {

			logger.error("mainExecutor(..) : ", e);
		}

	}
	
	/**
	 * insert the bulk record into mentioned table
	 * @param genTestedData - Generate test data
	 * @param tableName - The table need to insert the record
	 * @param columnDataArr- Column details (name, type, length etc ...)
	 */
	private void insertBulk(ArrayList<Object> genTestedData, String tableName, ArrayList<String> columnDataArr) {

		ArrayList<Object> splittedList = new ArrayList<>();
		int threshold = 999;//Limit the number of records per batch =threshold+1
		Object[] queryArgs;
		//Remove the spaces from the table name
		tableName = tableName.trim();
		//get the batch count, If you want to insert large volumes of data, enter the records in batches
		double numberOfLoops = Math.ceil((double) genTestedData.size() / (double) threshold);

		int loop = (int) numberOfLoops;//convert double to int
		int start = 0;
		int end = threshold;
		int listSize = genTestedData.size();// get the parsed data list size
		
		//Check the size of data list is greater than 1000 or not
		if (listSize >= 1000) {
			//If the size of data is greater than 1000, divide the list into 1000 sections
			for (int j = 0; j < loop; j++) {
				//sublist the array list 
				if ((end - start) != threshold || end > listSize) {
					ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, listSize));
					splittedList.add(innrSplitdList);
				} else {
				
					ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, end));
					splittedList.add(innrSplitdList);
				}

				start = end;
				end = end + threshold;

			}
			//Create a bulk string array to insert the records using column name and value list
			queryArgs = crtBlkQryStrng(columnDataArr, splittedList);
			
			//Get the list of values string
			ArrayList<Object> vals = (ArrayList<Object>) queryArgs[1];
			
			//Loop the values string list and parse the 
			for (int j = 0; j < vals.size(); j++) {
				Object[] singleBulkArgs = { queryArgs[0], vals.get(j) };//String array (column names string and value list string)
				gen.insrtTextData(singleBulkArgs, tableName);//Insert the test data to the table
			}

		} else {
			//Create a string array to insert the records using column name and value list
			String[] queryArg = crtQueryStrng(columnDataArr, genTestedData);
			
			//insert the records to the table
			gen.insrtData(queryArg, tableName);
		}

	}
}
