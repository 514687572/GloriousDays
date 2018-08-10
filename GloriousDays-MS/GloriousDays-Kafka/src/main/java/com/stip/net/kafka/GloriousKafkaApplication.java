package com.stip.net.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GloriousKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GloriousKafkaApplication.class, args);
	}
}
