package com.nacho.cryptomate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CryptomateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptomateApplication.class, args);
	}
}
