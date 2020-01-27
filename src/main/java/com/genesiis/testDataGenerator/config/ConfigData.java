/**
 * @author nipuna
 * 20191216 NJ XENO-97 - init config class
 * 20200127 RP XENO-94 - commenting
 */
package com.genesiis.testDataGenerator.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ConfigData {
  
	//Create a jdbc template bean class data source
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

   //Create a NamedParameterJdbcTemplate bean using data source
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    //Data source which is used to create a DB connection
    @Bean
    public DataSource testDataSource(@Autowired Environment env) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));// get driver class from application.property
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));// get db url from application.property
        dataSource.setUsername(env.getProperty("spring.datasource.user"));// get db user name from application.property
        dataSource.setPassword(env.getProperty("spring.datasource.pass"));// get db password from application.property

        return dataSource;
    }
}