package com.kafka.consumer.logging_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


// Generated by https://start.springboot.io
// 优质的 spring/boot/data/security/cloud 框架中文文档尽在 => https://springdoc.cn
@SpringBootApplication
@EnableAsync
public class LoggingServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		setIfNotNull("DB_PORT", dotenv.get("DB_PORT"));
		setIfNotNull("DB_NAME", dotenv.get("DB_NAME"));
		setIfNotNull("DB_USER", dotenv.get("DB_USER"));
		setIfNotNull("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(LoggingServiceApplication.class, args);
	}

	private static void setIfNotNull(String key, String value) {
		if (value != null) {
			System.setProperty(key, value);
		} else {
			System.out.println("⚠️ Warning: environment variable " + key + " is not set.");
		}
	}

}
