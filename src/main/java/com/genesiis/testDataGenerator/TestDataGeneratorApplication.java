/*
 * 20191209 NJ XENO-94 - INIT
 * 20191216 NJ XENO-94 - included dependency injection and removed default object creation
 * */
package com.genesiis.testDataGenerator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.Repository.impl.DataGenRepoImpl;
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
			getTestDataService().executeDataInsert();
		
	}
	
	

}
