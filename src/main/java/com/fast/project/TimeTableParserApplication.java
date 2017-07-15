package com.fast.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan(basePackages={"com.fast"})
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories("com.fast.project.repository")
public class TimeTableParserApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(TimeTableParserApplication.class, args);
	}
}
