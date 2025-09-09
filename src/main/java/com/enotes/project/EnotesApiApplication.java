package com.enotes.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class EnotesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnotesApiApplication.class, args);
	}

}
