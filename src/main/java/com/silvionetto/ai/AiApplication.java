package com.silvionetto.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class AiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

	@Scheduled(fixedRate = 5000)
	public void readEmail() {
		System.out.println("Reading email...");
	}

}
