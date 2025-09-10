package com.porvenir.logreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogreaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogreaderApplication.class, args);
		System.out.println("Hola Mundo");
	}

}
