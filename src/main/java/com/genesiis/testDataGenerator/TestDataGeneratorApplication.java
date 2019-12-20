/*
 * 20191209 NJ XENO-94 - INIT
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * 20191219 NJ XENO-94 - added Scanner to get the number of records to be inserted and the table name
 * */
package com.genesiis.testDataGenerator;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.genesiis.testDataGenerator.Service.TestDataService;
import com.genesiis.testDataGenerator.Service.impl.DataGenService;

@SpringBootApplication
public class TestDataGeneratorApplication implements CommandLineRunner{

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestDataGeneratorApplication.class, args);
		
		
	
	}
	
	@Bean
	public TestDataService getTestDataService() {
		
		return new DataGenService();
	}
	
	@Override
	public void run(String... args) throws Exception {
		
			Scanner in = new Scanner(System.in);
			System.out.println("**************************************************************************************");
			System.out.println("******************************TEST-DATA-GENERATOR*************************************");
			System.out.println("**************************************************************************************");
			System.out.print("Enter the number of test data to be generated : ");
			String numOfLoops = in.nextLine();
			System.out.println("");
			System.out.print("ENTER THE TABLE NAME : ");
			String tableName = in.nextLine();
			in.close();
			
			System.out.println("number of loops are "+numOfLoops);
			
			getTestDataService().executeDataInsert(numOfLoops,tableName);
		
	}
	
	

}
