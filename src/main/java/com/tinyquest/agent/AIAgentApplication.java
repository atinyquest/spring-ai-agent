package com.tinyquest.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.modulith.Modulith;
import org.springframework.modulith.core.ApplicationModules;

@Modulith
@SpringBootApplication
@ConfigurationPropertiesScan
public class AIAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIAgentApplication.class, args);
		ApplicationModules.of(AIAgentApplication.class).verify();
	}

}
