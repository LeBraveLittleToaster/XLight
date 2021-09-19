package de.pschiessle.xlight.xserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class XserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(XserverApplication.class, args);
	}

}