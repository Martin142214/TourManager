package com.example.TourManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TourManagerApplication {

	public static void main(String[] args) {
		System.setProperty("images.path", "E:/Java web projects/TourManager/src/main/resources/static/images/");

		SpringApplication.run(TourManagerApplication.class, args);
	}

}
