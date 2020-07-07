package com.example.stockapi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMongoRepositories
public class StockapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockapiApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public @Bean
	MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017");
	}

	public @Bean
	MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "stockapi");
	}
}
