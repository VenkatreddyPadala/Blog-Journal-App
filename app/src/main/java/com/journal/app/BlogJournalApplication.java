package com.journal.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BlogJournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogJournalApplication.class, args);
		System.out.println("Application started Successfully");
	}

}
