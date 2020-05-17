package com.spring.rajesh.junitexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.spring.rajesh")
public class JunitExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitExamplesApplication.class, args);
	}

}

