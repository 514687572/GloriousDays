package com.stip.net.user;

import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "com.stip",includeFilters = {@ComponentScan.Filter(Aspect.class)})
@EnableTransactionManagement(proxyTargetClass=true)
@MapperScan("com.stip.net.user.*") 
public class GloriousDaysUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(GloriousDaysUserApplication.class, args);
	}
}
