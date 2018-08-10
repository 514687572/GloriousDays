package com.stip.net.rabbitmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.stip.net.rabbitmq.annotation.StipCache;

/**
 *
 * @Title:
 * @date：2018年6月1日-上午11:43:36
 * @author：chenjunan
 *
 */
@Component
@RabbitListener(queues = "${spring.rabbitmq.queueName}")
public class TransactionListener {

	private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@StipCache(cacheName="transaction",cacheKey="message",messageArgMapper="message")
	@RabbitHandler
    public void receive(String message,Channel channel) {
		logger.debug("接收到消息" + message);
    }
	
}
