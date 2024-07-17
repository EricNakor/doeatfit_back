package com.Duo960118.fitow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication()
public class FitowApplication {
	public static void main(String[] args) {
		SpringApplication.run(FitowApplication.class, args);
	}
}
