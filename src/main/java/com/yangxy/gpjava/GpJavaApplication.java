package com.yangxy.gpjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = "com.yangxy.gpjava.token")
@SpringBootApplication
public class GpJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpJavaApplication.class, args);
	}

}
