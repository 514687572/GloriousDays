/**
 *
 * @date：2018年5月17日-下午1:20:00
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @Title:
 * @date：2018年5月17日-下午1:20:00
 * @author：chenjunan
 *
 */
@ComponentScan(basePackages = "com.stip",includeFilters = {@ComponentScan.Filter(Aspect.class)})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GloriousDaysControllerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GloriousDaysControllerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GloriousDaysControllerApplication.class, args);
	}

}
