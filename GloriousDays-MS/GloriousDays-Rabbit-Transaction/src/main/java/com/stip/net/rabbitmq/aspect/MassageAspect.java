/**
 *
 * @date：2018年6月1日-下午1:38:26
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.rabbitmq.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stip.net.common.service.redis.RedisService;

/**
 *
 * @Title:
 * @date：2018年6月1日-下午1:38:26
 * @author：chenjunan
 *
 */
@Order(1)  
@Aspect  
@Component  
public class MassageAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(MassageAspect.class);
	
	@Reference(version = "1.0.0")
	private RedisService redisService;
	
	/**
	 * 
	 * 把消息重新封装到MetaMessage中，MetaMessage包含了原交换机和路由键信息，便于重发
	 * 把封装后的MetaMessage放入缓存
	 * 
	 * @param point
	 * @throws Throwable 
	 */
	@Around("execution(* com.stip.net.rabbitmq.service.orm..send*Message(..))")
	public void aroundSendMessage(ProceedingJoinPoint point) throws Throwable{
    	logger.info("aopin测试缓存");
	}
	

}
