package com.example.account_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("account-service") // relative to root working dir
				.ignoreIfMissing()
				.load();


		setIfNotNull("DB_PORT", dotenv.get("DB_PORT"));
		setIfNotNull("DB_NAME", dotenv.get("DB_NAME"));
		setIfNotNull("DB_USER", dotenv.get("DB_USER"));
		setIfNotNull("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		setIfNotNull("SERVER_PORT", dotenv.get("SERVER_PORT"));

		SpringApplication.run(AccountServiceApplication.class, args);
	}

	private static void setIfNotNull(String key, String value) {
		if (value != null) {
			System.setProperty(key, value);
		} else {
			System.out.println("⚠️ Warning: environment variable " + key + " is not set.");
		}
	}


}
