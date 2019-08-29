package com.reactiveexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.reactiveexample.repository")
public class ReactiveExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveExampleApplication.class, args);
	}

}
