/*
 * 20191209 NJ XENO-94- init - created the service class and printout meta data
 * 20191212 NJ XENO-94 - added new case to switch as decimal and removed unwanted code
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
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
	
	
	
	@Override
	public HashMap<String, String> getColumnData(String tble) throws SQLException {

		ArrayList<MetaData> metaData = (ArrayList<MetaData>) gen.getTbleMetaData(tble);
		
		HashMap<String, String> columnData = new LinkedHashMap<>();
		
		for (MetaData metData : metaData) {
			if(!(metData.isAutoIncrement().equals("YES"))) {
			columnData.put(metData.getColumnName(), metData.getColumnTypeName());
			}
		}
		
		/*for(int i = 0;i<metaData.size();i++) {
			
			ArrayList<MetaData> innerArr = (ArrayList<MetaData>) metaData.get(i);
			
			if ((boolean) innerArr.get(3)) {
				metaData.removeAll(innerArr);
			} else {
		
				columnData.put(innerArr.get(0).toString(),innerArr.get(1).toString());
			
			}
		}*/
	
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


				MetaData metaObj = (MetaData) metaData.get(i); 
				if (!(metaObj.isAutoIncrement().equals("YES"))) {
					

					switch (metaObj.getColumnTypeName()) {

					case "int":

						data.add(dataGenTypes.getInt());
						genDataList.add(data);
						
						break;
						
					case "int identity":

						data.add(dataGenTypes.getInt());
						genDataList.add(data);
						
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar();
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
	    
	    		if(arr.get(1).toString() == "int" ||arr.get(1).toString() == "decimal") {
	    			
	    			
	    			str1.append(arr.get(j));
	    			
	    		}else {
	    			str1.append("\'");
	    			str1.append(arr.get(j));
	    			str1.append("\'");
	    			
	    		}
	    		if(j != arr.size()-1) {
	    		str1.append(",");
	    		}
	    	/*str1.append("?");
	    	if(j != params.size()-1) {
	    		str1.append(",");
	    		}*/
	    		
	    		
	    	
	      }
	      if(k == values.size()-1) {
		      str1.append(")");
		      }else {
		    	  str1.append("),");  
		      }
	     } 
	      
	      
	      
	     String queryParam = str.toString();
	     String vals = str1.toString();
	     
	     String [] QueryArgs = {queryParam,vals};
	     
		return QueryArgs;
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
	    
	    		if(arr.get(1).toString() == "int" ||arr.get(1).toString() == "decimal") {
	    			
	    			
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
	     String vals = "";
	     
	     
	     Object [] QueryArgs = {queryParam,valueList};
	     
		return QueryArgs;
	}

	@Override
	public void executeDataInsert(String numOfLoops,String tableName) throws SQLException {
		
		ArrayList<DbMetaData> dbm = getForiegnKeys(tableName);
		DbMetaData dbmObj =dbm.get(0);
		
		ArrayList<Object>splittedList = new ArrayList<>();
		Object [] QueryArgs = {};
		
		int loops = Integer.parseInt(numOfLoops);
		
		HashMap<String,String> columnData = getColumnData(tableName);
		ArrayList<Object> columnDataAr = new ArrayList<>(columnData.keySet());
		
		ArrayList<Object> genTestedData = null;
		try {
			
			ArrayList<Object> fkTblePkVals =gen.insertFkTbleDta(dbmObj.getFkParentTblName(),dbmObj.getPrimaryKey());
			
			ArrayList<MetaData> colData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
			
			if(columnData.containsKey(dbmObj.getFkParentTable())) {
				
				genTestedData =(ArrayList<Object>) genTestDtaMulti(loops,colData,fkTblePkVals,dbmObj.getFkParentTable());
				
			}else {
				
				genTestedData = (ArrayList<Object>) generateTstData(loops,tableName);
				
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		int threshold = 999;
		int i = 0;
		
		ArrayList<String> columnDataArr = new ArrayList<>(columnData.keySet());
		tableName=tableName.trim();
		
		double numberOfLoops = Math.ceil((double)genTestedData.size()/(double)threshold);
		
		int loop = (int)numberOfLoops;
		int start = 0;
		int end = threshold;
		
		
		
		
		/*if(genTestedData.size()>=1000) {
			for(int j =0;j<loop;j++) {
			
			if(j == (loop - 1)) {
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, genTestedData.size()));
				splittedList.add(innrSplitdList);
			}else {
				
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, end));
				splittedList.add(innrSplitdList);
			}
				
			
			start = end;
			end =end+end; 
		 
			}
			QueryArgs= crtBlkQryStrng(columnDataArr,splittedList);
			
			ArrayList<Object> vals = (ArrayList<Object>) QueryArgs[1];
			
			for(int j =0;j<vals.size();j++) {
				Object [] singleBulkArgs = {QueryArgs[0],vals.get(j)};
				gen.insrtTextData(singleBulkArgs,tableName);
			}
			
		}else {
			
			
			String [] QueryArg = (String[]) crtQueryStrng(columnDataAr,genTestedData);

			gen.insrtData(QueryArg,tableName);
		}*/
	
		insertBulk(genTestedData,tableName,columnDataArr);
		
		System.out.println("Data Generation was successfull");
		System.out.println(genTestedData.size()+" rows were generated!!!!!");
		
	}

	@Override
	public ArrayList<DbMetaData> getForiegnKeys(String tableName) throws SQLException {
		ArrayList<DbMetaData>dbMetaObj=(ArrayList<DbMetaData>) gen.getKeys(tableName);
		
		for (DbMetaData dbMetaData : dbMetaObj) {
			System.out.println("column Name : "+dbMetaData.getFkParentTable());
			System.out.println("table Name : "+dbMetaData.getFkParentTblName());
		}
		
		return dbMetaObj;
	}

	@Override
	public String[] getFKeyMeta(String mainTable) throws SQLException,Exception {
		
		ArrayList<DbMetaData>dbMetaObj = getForiegnKeys(mainTable);
		DbMetaData dbMeta = dbMetaObj.get(0);
		String tableName = dbMeta.getFkParentTblName();
		
		
		DbMetaData arr = dbMetaObj.get(0);
		String tbleName =arr.getFkParentTblName();
		
		ArrayList<String> columnDataArr = new ArrayList<>();
		ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
		for (MetaData metaData : columnData) {
			
			if(!metaData.isAutoIncrement().equals("YES")) {
				columnDataArr.add(metaData.getColumnName());
			}
		}
		ArrayList<Object> genTestedData = (ArrayList<Object>) generateTstData(columnDataArr.size(),mainTable);
	
		String [] QueryArg = (String[]) crtQueryStrng(columnDataArr,genTestedData);
		
		return QueryArg;
		
	}

	public Map<String, ArrayList<Object>> removeFColumn(int numOfLoops,String table) {
		

		Map<String, ArrayList<Object>>splitMap = new LinkedHashMap<>();
		ArrayList<Object>fKeyCol = new ArrayList<>();
		ArrayList<Object>norCol = new ArrayList<>();
		int indxOfPk = 0;
		
		try {
			//ArrayList<DbMetaData>dbMetaObj = getForiegnKeys();
			
			ArrayList<DbMetaData>dbMetaObj=(ArrayList<DbMetaData>) gen.getKeys(table);
			String pk = "";
			
			DbMetaData dbMeta = dbMetaObj.get(0);
			DbMetaData dbm =dbMetaObj.get(0);
			String tableName = dbm.getFkParentTable();
			
			ArrayList<Object> fkTblePkVals =gen.insertFkTbleDta(tableName,dbMeta.getPrimaryKey());
		
			ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.getTbleMetaData(tableName);
			ArrayList<Object>testData = (ArrayList<Object>) genTestDtaMulti(numOfLoops,columnData,fkTblePkVals,dbm.getFkParentTable());
			
			
			for (DbMetaData dbMetaData : dbMetaObj) {
				pk = dbMetaData.getPrimaryKey();
		
				for(int i=0;i<columnData.size();i++) {
					MetaData mData = columnData.get(i);
					if(mData.getColumnName().equals(pk)) {
						indxOfPk = i;
					}
				}
			
			 
				 
				 for(int i =0;i<testData.size();i++) {
					 ArrayList<Object>innerArr = new ArrayList<>();
					 innerArr = (ArrayList<Object>) testData.get(i);
					 fKeyCol.add(innerArr.get(indxOfPk));
					// innerArr.remove(indxOfPk);
					 norCol.add(innerArr);
					 
				 }
				 
				
			 splitMap.put("pkList", fKeyCol);
			 splitMap.put("colTstData",norCol);
			} 
			System.out.println("edwew");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return splitMap;
	}
	
	

	@Override
	public void removeFColumn() {
		// TODO Auto-generated method stub
		
	}

	public List<Object> genTestDtaMulti(int numOfLoops,ArrayList<MetaData> metaData,ArrayList<Object> pkList,String pk ) throws Exception {
		//ArrayList<Object> metaData = (ArrayList<Object>) gen.getTbleMetaData();
		
		ArrayList<Object> genDataList = new ArrayList<>();
		ArrayList<Object> genDataList1 = new ArrayList<>();
		
		
		int j = 0;
	
		while (j < numOfLoops) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {


				MetaData metaObj = (MetaData) metaData.get(i);
				
				if (!(metaObj.isAutoIncrement().equals("YES"))) {
					switch (metaObj.getColumnTypeName()) {

					case "int":
						
						if(metaObj.getColumnName().equals(pk)) {
							data.add(fkDataGenTypes.getInt(pkList));
							genDataList.add(data);
						}else {
						
							data.add(dataGenTypes.getInt());
							genDataList.add(data);
						}
						
						break;
						
					case "int identity":

						if(metaObj.getColumnName().equals(pk)) {
							data.add(fkDataGenTypes.getInt(pkList));
							genDataList.add(data);
						}else {
						
							data.add(dataGenTypes.getInt());
							genDataList.add(data);
						}
						
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar();
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

					}
				}	
				

			}
			genDataList1.add(data);
			j++;
			
		}
		
		return genDataList1;
	
		
	}
	
	public ArrayList<Object> generatePK(){
		
		ArrayList<Object> result = new ArrayList<>();
		
		return result;
	}

	@Override
	public Map<String, ArrayList<Object>> removeFColumn(int numOfLoops) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void mainExecutor(String numOfLoops,String mainTable) throws Exception {
			
			try {
				ArrayList<DbMetaData>dbMetaObj = getForiegnKeys(mainTable);
				ArrayList<Object> testData = new ArrayList<>();
				ArrayList colData = new ArrayList<>();
				
				int loops=Integer.parseInt(numOfLoops);
				
				
				if(!(dbMetaObj == null || dbMetaObj.isEmpty())) {
					DbMetaData arr = dbMetaObj.get(0);
					String tableName =arr.getFkParentTblName();
					String pk = arr.getPrimaryKey();
					
					ArrayList<MetaData> columnData = (ArrayList<MetaData>) gen.gtFkTbleMetaDta(tableName);
					
					for (MetaData metaData : columnData) {
						if(!metaData.getColumnName().equals(pk)) {
						colData.add(metaData.getColumnName());
						}
					}
					
					LinkedHashMap<String, ArrayList<Object>> result = (LinkedHashMap<String, ArrayList<Object>>) removeFColumn(loops,mainTable);
					
					
					testData = result.get("colTstData");
					
					
					
					//String[]queryString = getFKeyMeta();
					
					
					
					String [] QueryArg = (String[]) crtQueryStrng(colData,testData);
					
					
					
					insertBulk(testData, tableName,colData);
					
					executeDataInsert(numOfLoops,mainTable);
					
				}else {
					executeDataInsert(numOfLoops,mainTable);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	private void insertBulk(ArrayList<Object>genTestedData,String tableName,ArrayList<String> columnDataArr) {
		
		ArrayList<Object>splittedList = new ArrayList<>();
		int threshold = 999;
		int i = 0;
		Object [] QueryArgs = {};
		
		//r = new ArrayList<>(columnData.keySet());
		tableName=tableName.trim();
		
		double numberOfLoops = Math.ceil((double)genTestedData.size()/(double)threshold);
		
		int loop = (int)numberOfLoops;
		int start = 0;
		int end = threshold;
		
		if(genTestedData.size()>=1000) {
			for(int j =0;j<loop;j++) {
			
			if(j == (loop - 1)) {
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, genTestedData.size()));
				splittedList.add(innrSplitdList);
			}else {
				
				ArrayList<Object> innrSplitdList = new ArrayList(genTestedData.subList(start, end));
				splittedList.add(innrSplitdList);
			}
				
			
			start = end;
			end =end+end; 
		 
			}
			QueryArgs= crtBlkQryStrng(columnDataArr,splittedList);
			
			ArrayList<Object> vals = (ArrayList<Object>) QueryArgs[1];
			
			for(int j =0;j<vals.size();j++) {
				Object [] singleBulkArgs = {QueryArgs[0],vals.get(j)};
				gen.insrtTextData(singleBulkArgs,tableName);
			}
			
		}else {
			
			
			String [] QueryArg = (String[]) crtQueryStrng(columnDataArr,genTestedData);

			gen.insrtData(QueryArg,tableName);
		}
	
		
		System.out.println("Data Generation was successfull");
		System.out.println(genTestedData.size()+" rows were generated!!!!!");
		
	}
}
