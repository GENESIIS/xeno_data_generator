/*
 * 20191209 NJ XENO-97- init - created the service class and printout meta data
 * */
package com.genesiis.testDataGenerator.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.genesiis.testDataGenerator.Repository.DataGenRepo;

public class DataGenService {
	
 public void extrctMetaData() throws Exception {
	 
		DataGenRepo gen = new DataGenRepo();
		gen.metaData();
		
		for(int i =0;i<gen.metaData().size();i++) {
		      ArrayList gg = (ArrayList) gen.metaData().get(i);
		      System.out.println("Column name: "+gg.get(0));
		      System.out.println("Column type: "+gg.get(1));
		      System.out.println("Column size: "+gg.get(2));
		      System.out.println("Column is Auto Incremented: "+gg.get(3));
		      System.out.println("column nullable : "+gg.get(4));
		      System.out.println(" ");
		   
		    	  
		      }

 }
	

}
