package com.Duo960118.fitow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FitowApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitowApplication.class, args);
	}

}
