/**
 *
 * @date：2018年6月1日-下午1:44:38
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.rabbitmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @Title:
 * @date：2018年6月1日-下午1:44:38
 * @author：chenjunan
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StipCache {
	public String cacheName() default "";
	public String cacheKey();
	public String messageArgMapper(); //不支持spel，默认为${arg.xxx}
	public String routingKey() default "";
	public String exchange() default "";
}
