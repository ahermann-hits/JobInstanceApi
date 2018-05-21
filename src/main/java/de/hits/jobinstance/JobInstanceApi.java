package de.hits.jobinstance;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Configuration
@PropertySource(value = "file:/var/data/JobInstance/api/application-${spring.profiles.active}.properties", ignoreResourceNotFound = false)
@SpringBootApplication
@EnableAutoConfiguration
public class JobInstanceApi implements CommandLineRunner {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(JobInstanceApi.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Used datasource = " + dataSource);
	}
}