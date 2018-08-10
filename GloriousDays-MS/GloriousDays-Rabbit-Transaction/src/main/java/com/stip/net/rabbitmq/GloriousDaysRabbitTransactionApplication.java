package com.stip.net.rabbitmq;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.stip",includeFilters = {@ComponentScan.Filter(Aspect.class)})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GloriousDaysRabbitTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GloriousDaysRabbitTransactionApplication.class, args);
	}
}
