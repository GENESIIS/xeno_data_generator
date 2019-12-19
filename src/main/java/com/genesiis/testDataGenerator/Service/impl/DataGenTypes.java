/**
 *20191216 NJ XENO-94 - init and added methods to generate test data according to datatypes
 */
package com.genesiis.testDataGenerator.Service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

/**
 * @author nipuna
 *
 */
@Component
public class DataGenTypes {
	
	static Random r;
	
	//generate values for columns having the datatype varchar
	public String genVarchar() {
		
		int leftLimit = 97; // letter 'a'
  	    int rightLimit = 122; // letter 'z'
  	    int targetStringLength = 10;
  	    Random random1 = new Random();
  	    StringBuilder buffer = new StringBuilder(targetStringLength);
  	    for (int j = 0; j < targetStringLength; j++) {
  	        int randomLimitedInt = leftLimit + (int) 
  	          (random1.nextFloat() * (rightLimit - leftLimit + 1));
  	        buffer.append((char) randomLimitedInt);
  	    }
  	    
  	    return buffer.toString();
		
	}
	//generate values for columns having the datatype int
	public int getInt() {
		int random = (int)(Math.random()*50+1);
      	System.out.println("generated int : "+random);
		
		return random;
		
	}
	//generate values for columns having the datatype dateTime
	public Date getDate() {
		
  		Date dt;
  		long ms;
  		// Get a new random instance, seeded from the clock
  		r = new Random();

  		// Get an Epoch value roughly between 1940 and 2010
  		// -946771200000L = January 1, 1940
  		// Add up to 70 years to it (using modulus on the next long)
  		ms = -946771200000L + (Math.abs(r.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));

  		// Construct a date
  		dt = new Date(ms);
  
		return dt;
	}
	
	public char getChar() {
		
  		char chr = (char)(r.nextInt(26) + 'a');
  		
  		System.out.println("char is : "+chr);
  		
  		return chr;
	}
	public long getDecimal(ArrayList arr) {
		
  		int precision = (int)arr.get(5);
  		int scale = (int)arr.get(6);
  		int num = precision-scale;
  		
  		long min = (long) Math.pow(10, num - 1);
		return ThreadLocalRandom.current().nextLong(min, min * 10);
  		
	}
	
}
