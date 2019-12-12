/*
 * 20191209 NJ XENO-97- init - created the service class and printout meta data
 * 20191212 NJ XENO-97 - added new case to switch as decimal and removed unwanted code
 * */
package com.genesiis.testDataGenerator.Service;

import java.util.ArrayList;
import com.genesiis.testDataGenerator.Repository.DataGenRepo;

public class DataGenService {
	
	public void generateData() throws Exception {

		DataGenTypes dataGenTypes = new DataGenTypes();
		DataGenRepo gen = new DataGenRepo();
		ArrayList<Object> finalList = new ArrayList<>();
		ArrayList<Object> dataList = new ArrayList<>();

		ArrayList<Object> metaData = (ArrayList<Object>) gen.metaData();
		int j = 0;
		while (j < 100) {
			for (int i = 0; i < metaData.size(); i++) {

				ArrayList<Object> result = new ArrayList<>();

				ArrayList<Object> data = new ArrayList<>();

				ArrayList<Object> innerArr = (ArrayList<Object>) metaData.get(i);
				System.out.println("Column name: " + innerArr.get(0));
				System.out.println("Column type: " + innerArr.get(1));
				System.out.println("Column size: " + innerArr.get(2));
				System.out.println("Column is Auto Incremented: " + innerArr.get(3));
				System.out.println("column nullable : " + innerArr.get(4));
				System.out.println("column precision : " + innerArr.get(5));
				System.out.println("column scale : " + innerArr.get(6));
				System.out.println(" ");

				if ((boolean) innerArr.get(3)) {
					metaData.removeAll(innerArr);
				} else {

					switch (innerArr.get(1).toString()) {

					case "int":

						data.add(dataGenTypes.getInt());
						data.add(innerArr.get(1));
						dataList.add(data);
						finalList.add(innerArr.get(0).toString());
						break;

					case "varchar":

						String generatedString = dataGenTypes.genVarchar();
						data.add(generatedString);
						data.add(innerArr.get(1));
						dataList.add(data);
						finalList.add(innerArr.get(0).toString());
						break;

					case "datetime":

						data.add(dataGenTypes.getDate());
						data.add(innerArr.get(1));
						dataList.add(data);
						finalList.add(innerArr.get(0).toString());
						break;

					case "char":

						data.add(dataGenTypes.getChar());
						data.add(innerArr.get(1));
						dataList.add(data);
						finalList.add(innerArr.get(0).toString());
						break;

					case "decimal":

						data.add(dataGenTypes.getDecimal(innerArr));
						data.add(innerArr.get(1));
						dataList.add(data);
						finalList.add(innerArr.get(0).toString());
						break;

					}
				}
				System.out.println("=======================================================");
				System.err.println("printing final list : " + result);

				System.err.println("printing data list : " + data);

			}
			System.err.println("final List " + finalList);
			System.err.println("printing data list : " + dataList);
			gen.insertData(finalList, dataList);
			
			finalList.clear();
			dataList.clear();
			j++;

		}
	}
	
	public String[] crtQueryStrng(ArrayList params,ArrayList values) {
		
	      StringBuilder str = new StringBuilder();
	      StringBuilder str1 = new StringBuilder();
	      
	      for(int i = 0;i<params.size();i++) {
	    	 
	    	 
	    	  str.append(params.get(i));
	    	  if(i<params.size()-1) {
	    		  str.append(",");
	    	  }
	      }
	      
	      
	      for(int j =0;j<values.size();j++) {
	    	ArrayList arr =(ArrayList)values.get(j);
	    	
	    		
	    		if(arr.get(1).toString() == "int" ||arr.get(1).toString() == "decimal") {
	    			
	    			
	    			str1.append(arr.get(0));
	    			
	    		}else {
	    			str1.append("\'");
	    			str1.append(arr.get(0));
	    			str1.append("\'");
	    			
	    		}
	    		if(j != values.size()-1) {
	    		str1.append(",");
	    		}
	      }
	      
	     String queryParam = str.toString();
	     String vals = str1.toString();
	     
	     String [] QueryArgs = {queryParam,vals};
	     
		return QueryArgs;
	}

}
