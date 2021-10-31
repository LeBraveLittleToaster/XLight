package de.pschiessle.xlight.xlightserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
public class XlightserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(XlightserverApplication.class, args);
	}

}
