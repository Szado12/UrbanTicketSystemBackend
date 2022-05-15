package com.piisw.UrbanTicketSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class UrbanTicketSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(UrbanTicketSystemApplication.class, args);
	}

}
