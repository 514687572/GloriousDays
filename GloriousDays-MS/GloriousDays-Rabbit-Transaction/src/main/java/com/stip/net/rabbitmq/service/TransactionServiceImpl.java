/**
 *
 * @date：2018年6月1日-下午1:18:16
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.rabbitmq.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.stip.net.common.service.transaction.TransactionService;
import com.stip.net.rabbitmq.RabbitConfiguration;

/**
 *
 * @Title:发送事务消息
 * @date：2018年6月1日-下午1:18:16
 * @author：chenjunan
 *
 */
@org.springframework.stereotype.Service
@Service(version = "1.0.0")
public class TransactionServiceImpl implements TransactionService {
	private String orderSaveKey=RabbitConfiguration.ROUNTING_KEY_PREFIX+"."+RabbitConfiguration.ORDER_SAVE_ROUTING_KEY;
	
	@Value("${spring.rabbitmq.exchange}")
	private String orderExchange;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private TransactionTemplate transactionTemplate;

	/* (non-Javadoc)
	 * @see com.stip.net.common.service.transaction.TransactionService#sendMessage(java.lang.String, java.lang.Object)
	 */
	@Override
	public void sendMessage(String correlationId, Object order) throws Exception {
		CorrelationData correlation = new CorrelationData(correlationId);
		Message msg=new Message(JSONObject.toJSONString(order).getBytes(),MessagePropertiesBuilder.newInstance().setContentType("text/x-json").build());//.setExpiration("10000").build());
		rabbitTemplate.send(orderExchange, orderSaveKey, msg, correlation);
	}

	/* (non-Javadoc)
	 * @see com.stip.net.common.service.transaction.TransactionService#sendTransactionMessage(java.lang.String, java.lang.Object)
	 */
	@Override
	public String sendTransactionMessage(String queueName,String correlationId, Object order) throws Exception {
		String result = transactionTemplate.execute(new TransactionCallback<String>() {
	        @Override
	        public String doInTransaction(TransactionStatus status) {
	    		CorrelationData correlation = new CorrelationData(correlationId);
	    		Message msg=new Message(JSONObject.toJSONString(order).getBytes(),MessagePropertiesBuilder.newInstance().setContentType("text/x-json").build());
	        	rabbitTemplate.convertAndSend(orderExchange,orderSaveKey,msg, correlation);
	        	
	            return (String) rabbitTemplate.receiveAndConvert(queueName);
	        }
	    });
		
		return result;
	}

}
