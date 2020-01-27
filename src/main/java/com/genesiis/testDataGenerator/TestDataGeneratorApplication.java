/*
 * 20191209 NJ XENO-94 - INIT
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * 20191219 NJ XENO-94 - added Scanner to get the number of records to be inserted and the table name
 * 20200127 RP XENO-94 - commenting
 * */
package com.genesiis.testDataGenerator;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.genesiis.testDataGenerator.Service.TestDataService;
import com.genesiis.testDataGenerator.Service.impl.DataGenService;

@SpringBootApplication
public class TestDataGeneratorApplication implements CommandLineRunner{
	
	private static final  Logger logger = LogManager.getLogger(TestDataGeneratorApplication.class);

	//main method of the application 
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
			logger.info("**************************************************************************************");
			logger.info("******************************TEST-DATA-GENERATOR*************************************");
			logger.info("**************************************************************************************");
			logger.info("Enter the number of test data to be generated : ");
			String numOfLoops = in.nextLine();
			logger.info("Enter the table name : ");
			String tableName = in.nextLine();
			in.close();
		
			getTestDataService().mainExecutor(numOfLoops,tableName);
		
	}
	
	

}
