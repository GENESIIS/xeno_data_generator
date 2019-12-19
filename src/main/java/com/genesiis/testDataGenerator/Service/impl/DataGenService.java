/*
 * 20191209 NJ XENO-94- init - created the service class and printout meta data
 * 20191212 NJ XENO-94 - added new case to switch as decimal and removed unwanted code
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * */
package com.genesiis.testDataGenerator.Service.impl;

/**
 * @author nipuna
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.Repository.impl.DataGenRepoImpl;
import com.genesiis.testDataGenerator.Service.TestDataService;

@Service
public class DataGenService implements TestDataService{
	
	@Autowired
	DataGenTypes dataGenTypes;
	@Autowired
	DataGenRepo gen;
	
	
	
	@Override
	public HashMap<String, String> getColumnData() {
		
		  ArrayList<Object> metaData = (ArrayList<Object>) gen.getTbleMetaData();
		
		HashMap<String, String> columnData = new LinkedHashMap<>();
		
		for(int i = 0;i<metaData.size();i++) {
			
			ArrayList<Object> innerArr = (ArrayList<Object>) metaData.get(i);
			
			if ((boolean) innerArr.get(3)) {
				metaData.removeAll(innerArr);
			} else {
		
				columnData.put(innerArr.get(0).toString(),innerArr.get(1).toString());
			
			}
		}
	
		return columnData;
	}
	
	@Override
	public List<Object> generateTstData() throws Exception {
		ArrayList<Object> metaData = (ArrayList<Object>) gen.getTbleMetaData();
		
		ArrayList<Object> genDataList = new ArrayList<>();
		ArrayList<Object> genDataList1 = new ArrayList<>();
		
		
		int j = 0;
	
		while (j < 200) {
			ArrayList<Object> data = new ArrayList<>();
			for (int i = 0; i < metaData.size(); i++) {
				
				ArrayList<Object> data1 = new ArrayList<>();

				ArrayList<Object> innerArr = (ArrayList<Object>) metaData.get(i);
				if ((boolean) innerArr.get(3)) {
					metaData.removeAll(innerArr);
				} else {

					switch (innerArr.get(1).toString()) {

					case "int":

						data.add(dataGenTypes.getInt());
						//data1.add(innerArr.get(1).toString());
						//data.add(data1);
						genDataList.add(data);
						
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar();
						data.add(generatedString);
						//data1.add(innerArr.get(1).toString());
						//data.add(data1);
						genDataList.add(data);
					
						break;

					case "datetime":

						data.add(dataGenTypes.getDate());
						//data1.add(innerArr.get(1).toString());
						//data.add(data1);
						genDataList.add(data);
					
						break;

					case "char":

						data.add(dataGenTypes.getChar());
						//data1.add(innerArr.get(1).toString());
						//data.add(data1);
						genDataList.add(data);
					
						break;

					case "decimal":

						data.add(dataGenTypes.getDecimal(innerArr));
						//data1.add(innerArr.get(1).toString());
						//data.add(data1);
						genDataList.add(data);
					
						break;

					}
					
				}

			}
			
			//executeDataInsert(dataList);
			genDataList1.add(data);
			j++;
			
		}
		System.out.println("gentDataList1 "+genDataList1);
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
	public void executeDataInsert() {
		
		HashMap<String,String> columnData = getColumnData();
		
		ArrayList<Object> genTestedData = null;
		try {
			genTestedData = (ArrayList<Object>) generateTstData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		ArrayList<String> columnDataArr = new ArrayList<>(columnData.keySet());
		
		String [] QueryArgs = crtQueryStrng(columnDataArr,genTestedData);
		
		gen.insrtTextData(QueryArgs);
		try {
			//gen.insrtTextData(QueryArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
