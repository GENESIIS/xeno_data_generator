/*
 * 20191209 NJ XENO-97 - INIT
 * */
package com.genesiis.testDataGenerator;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.genesiis.testDataGenerator.Repository.DataGenRepo;
import com.genesiis.testDataGenerator.Service.DataGenService;

@SpringBootApplication
public class TestDataGeneratorApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestDataGeneratorApplication.class, args);
		
		DataGenService dataGenService = new DataGenService();
	
			dataGenService.generateData();
			
		
	}

}
