/*
 * 20191209 NJ XENO-94- init - created the service class and printout meta data
 * 20191212 NJ XENO-94 - added new case to switch as decimal and removed unwanted code
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * 20200106 NJ XENO-94 - changed methods to generate and insert data to sub tables of a parent table binded by a  foreign key
 * 20200107 NJ XENO-94 - removed unused codes and fixed sonalrlint issues
 * 
 * */
package com.genesiis.testDataGenerator.Service.impl;

import java.sql.SQLException;

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
public class DataGenService implements TestDataService{
	
	@Autowired
	DataGenTypes dataGenTypes;
	@Autowired
	DataGenRepo gen;
	@Autowired
	FkDataGenTypes fkDataGenTypes;
	
	private static final Logger logger = LogManager.getLogger(DataGenService.class);

	@Override
	public HashMap<String, String> getColumnData(String tble) throws SQLException {

		ArrayList<MetaData> metaData = (ArrayList<MetaData>) gen.getTbleMetaData(tble);
		
		HashMap<String, String> columnData = new LinkedHashMap<>();
		
		for (MetaData metData : metaData) {
			if(!(metData.isAutoIncrement().equals("true"))) {
			columnData.put(metData.getColumnName(), metData.getColumnTypeName());
			}
		}
	
		return columnData;
	}
	
	@Override
	public List<Object> generateTstData(int numOfLoops,String tableName) throws Exception {
		ArrayList<MetaData> metaData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
		
		ArrayList<Object> genDataList = new ArrayList<>();
		ArrayList<Object> genDataList1 = new ArrayList<>();
		int j = 0;
	
		while (j < numOfLoops) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {

				MetaData metaObj =  metaData.get(i); 
				if (!(metaObj.isAutoIncrement().equals("true"))) {
					
					switch (metaObj.getColumnTypeName()) {

					case "int":	
					case "int identity":

						data.add(dataGenTypes.getInt());
						genDataList.add(data);
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar(metaObj.getColumnSize());
						data.add(generatedString);
						genDataList.add(data);
						break;

					case "datetime":

						data.add(dataGenTypes.getDateTime());
						genDataList.add(data);				
						break;
						
					case "date":

						data.add(dataGenTypes.getDate());
						genDataList.add(data);					
						break;

					case "char":

						data.add(dataGenTypes.getChar());
						genDataList.add(data);					
						break;

					case "decimal":

						data.add(dataGenTypes.getDecimal(metaObj));
						genDataList.add(data);					
						break;
						
					default :

					}
					
				}

			}
			genDataList1.add(data);
			j++;
			
		}
		
		return genDataList1;	
	}
	
	@Override
	public String[] crtQueryStrng(ArrayList params,ArrayList values) {
		
	      StringBuilder str = new StringBuilder();
	      StringBuilder str1 = new StringBuilder();
	      
	      for(int i = 0;i<params.size();i++) {
	    	 
	    	 
	    	  str.append(params.get(i));
	    	  if(i<params.size()-1) {
	    		  str.append(",");
	    	  }
	      }
	      int b = 0;
	      
	      
	      
	     for(int k =0;k<values.size();k++) {
	      str1.append("(");
	      
	      ArrayList<Object> arr =(ArrayList<Object>) values.get(k); 
	      int arrSize=arr.size();
		      for(int j =0;j<arrSize;j++) {
		    
		    		if(arr.get(1).toString().equals("int") ||arr.get(1).toString().equals("decimal")) {
		
		    			str1.append(arr.get(j));
		    			
		    		}else {
		    			str1.append("\'");
		    			str1.append(arr.get(j));
		    			str1.append("\'");
		    			
		    		}
		    		if(j != arr.size()-1) {
		    		str1.append(",");
		    		}
		      }
		      if(k == values.size()-1) {
			      str1.append(")");
			      }else {
			    	  str1.append("),");  
			      }
	     } 
	      
	      
	      
	     String queryParam = str.toString();
	     String vals = str1.toString();
	     
	     String [] queryArgs = {queryParam,vals};
	     
		return queryArgs;
	}
	
	@Override
	public Object[] crtBlkQryStrng(ArrayList params,ArrayList values) {
		
	      StringBuilder str = new StringBuilder();
	      
	      ArrayList<String> valueList = new ArrayList<>();
	      
	      for(int i = 0;i<params.size();i++) {
	    	 
	    	 
	    	  str.append(params.get(i));
	    	  if(i<params.size()-1) {
	    		  str.append(",");
	    	  }
	      }
	      int b = 0;
	      
	     for(int m=0;m<values.size();m++) {
	    	 
	    	 StringBuilder str1 = new StringBuilder();
	    	ArrayList<Object> arr1 = new ArrayList<>();
	    	arr1 =(ArrayList) values.get(m);
	      
	     for(int k =0;k<arr1.size();k++) {
	      
	      str1.append("(");
	
	      ArrayList<Object> arr =(ArrayList<Object>) arr1.get(k); 
	      int arrSize=arr.size();
	      for(int j =0;j<arrSize;j++) {
	    
	    		if(arr.get(1).toString().equals("int") ||arr.get(1).toString().equals("decimal")) {
	    			
	    			str1.append(arr.get(j));
	    			
	    		}else {
	    			str1.append("\'");
	    			str1.append(arr.get(j));
	    			str1.append("\'");
	    			
	    		}
	    		if(j != arr.size()-1) {
	    		str1.append(",");
	    		}
	    
	      }
	      if(k == arr1.size()-1) {
		      str1.append(")");
		      }else {
		    	  str1.append("),");  
		      }
	     } 
	     String vals = str1.toString();
	     valueList.add(vals);
	     }  
	      
	      
	     String queryParam = str.toString();
	 
	     Object [] queryArgs = {queryParam,valueList};
	     
		return queryArgs;
	}

	@Override
	public void executeDataInsert(String numOfLoops,String tableName) throws Exception {
		
		ArrayList<DbMetaData> dbm = getForiegnKeys(tableName);
		ArrayList<Object>fkTblePkVals = new ArrayList<>();
		ArrayList<String>parentTables = new ArrayList<>();
		
		int loops = Integer.parseInt(numOfLoops);
		HashMap<String,String> columnData = getColumnData(tableName);
		ArrayList<Object> genTestedData = null;
		
		ArrayList<MetaData> colData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
		
		if(dbm == null ||dbm.isEmpty()) {
			
			try {
				genTestedData = (ArrayList<Object>) generateTstData(loops,tableName);
			} catch (Exception e) {
	
				e.printStackTrace();
			}
		}else {
			
			
				
			for(int i = 0;i<dbm.size();i++) {
				DbMetaData dbmObj =dbm.get(i);
				ArrayList<Object> fkTblePkVal =gen.insertFkTbleDta(dbmObj.getFkParentTblName(),dbmObj.getPrimaryKey());
				parentTables.add(dbmObj.getFkParentTable());
				fkTblePkVals.add(fkTblePkVal);
				
			}

					genTestedData =(ArrayList<Object>) genTestDtaMulti(loops,colData,fkTblePkVals,parentTables);

		}
	
		
		ArrayList<String> columnDataArr = new ArrayList<>(columnData.keySet());
		tableName=tableName.trim();
		insertBulk(genTestedData,tableName,columnDataArr);
		
		logger.info("Data Generation was successfull");
		logger.info(genTestedData.size()+" rows were generated!!!!!");
		
	}

	@Override
	public ArrayList<DbMetaData> getForiegnKeys(String tableName) throws SQLException {
		return (ArrayList<DbMetaData>) gen.getKeys(tableName);
	}

	@Override
	public String[] getFKeyMeta(String mainTable) throws SQLException,Exception {
		
		ArrayList<DbMetaData>dbMetaObj = getForiegnKeys(mainTable);
		DbMetaData dbMeta = dbMetaObj.get(0);
		String tableName = dbMeta.getFkParentTblName();
	
		ArrayList<String> columnDataArr = new ArrayList<>();
		ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
		for (MetaData metaData : columnData) {
			
			if(!metaData.isAutoIncrement().equals("yes")) {
				columnDataArr.add(metaData.getColumnName());
			}
		}
		ArrayList<Object> genTestedData = (ArrayList<Object>) generateTstData(columnDataArr.size(),mainTable);

		return crtQueryStrng(columnDataArr,genTestedData);
		
	}

	public Map<String, ArrayList<Object>> removeFColumn(int numOfLoops,String table,DbMetaData dbm) {
		

		Map<String, ArrayList<Object>>splitMap = new LinkedHashMap<>();
		ArrayList<Object>fKeyCol = new ArrayList<>();
		ArrayList<Object>norCol = new ArrayList<>();
		int indxOfPk = 0;
		
		try {
			
			ArrayList<DbMetaData>dbMetaObj=(ArrayList<DbMetaData>) gen.getKeys(table);
			String pk = "";
			
			ArrayList<String> tableNames =new ArrayList<>() ;
			tableNames.add(dbm.getFkParentTable());
			
			ArrayList<Object> fkTblePkVals =gen.insertFkTbleDta(tableNames.get(0),dbm.getPrimaryKey());
		
			ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableNames.get(0));
			ArrayList<Object>testData = (ArrayList<Object>) genTestDtaMulti(numOfLoops,columnData,fkTblePkVals,tableNames);
			
			 splitMap.put("colTstData",testData);
			for (DbMetaData dbMetaData : dbMetaObj) {
				pk = dbMetaData.getPrimaryKey();
		
				for(int i=0;i<columnData.size();i++) {
					MetaData mData = columnData.get(i);
					if(mData.getColumnName().equals(pk)) {
						indxOfPk = i;
					}
				}
			
			 
				 
				 for(int i =0;i<testData.size();i++) {
					 ArrayList<Object>innerArr;
					 innerArr = (ArrayList<Object>) testData.get(i);
					 fKeyCol.add(innerArr.get(indxOfPk));
					 norCol.add(innerArr);
					 
				 }
				 
				
			 splitMap.put("pkList", fKeyCol);
			 splitMap.put("colTstData",testData);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return splitMap;
	}


	public List<Object> genTestDtaMulti(int numOfLoops,List<MetaData> metaData,List<Object> pkList,List<String> pk ) throws Exception {
		
		ArrayList<Object> genDataList = new ArrayList<>();
		ArrayList<Object> genDataList1 = new ArrayList<>();
		
		
		int j = 0;
	
		while (j < numOfLoops) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {


				MetaData metaObj =  metaData.get(i);
				
				if (!(metaObj.isAutoIncrement().equals("true"))) {
					switch (metaObj.getColumnTypeName()) {

					case "int":
						
						if(pk.contains(metaObj.getColumnName())) {
							int location = pk.indexOf(metaObj.getColumnName());
							
							data.add(fkDataGenTypes.getInt((ArrayList<Object>) pkList.get(location)));
							genDataList.add(data);
						}else {
						
							data.add(dataGenTypes.getInt());
							genDataList.add(data);
						}
						
						break;
						
					case "int identity":

						if(metaObj.getColumnName().equals(pk)) {
							data.add(fkDataGenTypes.getInt((ArrayList<Object>) pkList));
							genDataList.add(data);
						}else {
						
							data.add(dataGenTypes.getInt());
							genDataList.add(data);
						}
						
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar(metaObj.getColumnSize());
						data.add(generatedString);
						genDataList.add(data);
					
						break;

					case "datetime":

						data.add(dataGenTypes.getDateTime());
						genDataList.add(data);
					
						break;
						
					case "date":

						data.add(dataGenTypes.getDate());
						genDataList.add(data);
					
						break;

					case "char":

						data.add(dataGenTypes.getChar());
						genDataList.add(data);
					
						break;

					case "decimal":

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
		
		return genDataList1;
	
		
	}
	
	public void mainExecutor(String numOfLoops,String mainTable) throws Exception {
			
			try {
				ArrayList<DbMetaData>dbMetaObj = getForiegnKeys(mainTable);
				ArrayList<Object> testData;
				
				
				int loops=Integer.parseInt(numOfLoops);
				
				for(int i =0;i<dbMetaObj.size();i++) {
					ArrayList colData = new ArrayList<>();
					
						DbMetaData arr = dbMetaObj.get(i);
						String tableName =arr.getFkParentTblName();
						String pk = arr.getPrimaryKey();
						
						ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.gtFkTbleMetaDta(tableName);
						
						for (MetaData metaData : columnData) {
							if(!metaData.getColumnName().equals(pk)) {
							colData.add(metaData.getColumnName());
							}
						}
						
						LinkedHashMap<String, ArrayList<Object>> result = (LinkedHashMap<String, ArrayList<Object>>) removeFColumn(loops,tableName,arr);
						
						
						testData = result.get("colTstData");
				
						insertBulk(testData, tableName,colData);
						
					
				}
				executeDataInsert(numOfLoops,mainTable);
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			
	}
	
	private void insertBulk(ArrayList<Object>genTestedData,String tableName,ArrayList<String> columnDataArr) {
		
		ArrayList<Object>splittedList = new ArrayList<>();
		int threshold = 999;
		int i = 0;
		Object [] queryArgs ;
		
		tableName=tableName.trim();
		
		double numberOfLoops = Math.ceil((double)genTestedData.size()/(double)threshold);
		
		int loop = (int)numberOfLoops;
		int start = 0;
		int end = threshold;
		int listSize = genTestedData.size();
		
		if(listSize>=1000) {
			for(int j =0;j<loop;j++) {
			
			if((end-start) !=threshold || end>listSize) {
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, listSize));
				splittedList.add(innrSplitdList);
			}else {
			
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, end));
				splittedList.add(innrSplitdList);
			}
				
			
			start = end;
			end =end+threshold; 
		 
			}
			queryArgs= crtBlkQryStrng(columnDataArr,splittedList);
			
			ArrayList<Object> vals = (ArrayList<Object>) queryArgs[1];
			
			for(int j =0;j<vals.size();j++) {
				Object [] singleBulkArgs = {queryArgs[0],vals.get(j)};
				gen.insrtTextData(singleBulkArgs,tableName);
			}
			
		}else {
			
			
			String [] queryArg = crtQueryStrng(columnDataArr,genTestedData);

			gen.insrtData(queryArg,tableName);
		}
	
		
	}
}
