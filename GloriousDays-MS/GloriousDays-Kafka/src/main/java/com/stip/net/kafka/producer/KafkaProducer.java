/**
 *
 * @date：2018年6月8日-下午1:34:59
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.stip.net.common.pojo.SampleMessage;
import com.stip.net.common.service.kafka.KafkaProducerService;

/**
 *
 * @Title:
 * @date：2018年6月8日-下午1:34:59
 * @author：chenjunan
 *
 */
@Service(version = "1.0.0")
public class KafkaProducer implements KafkaProducerService{
	
	@Autowired
	private KafkaTemplate<Object, SampleMessage> kafkaTemplate;

	public void send(SampleMessage message) {
		this.kafkaTemplate.send("logstash", message);
		System.out.println("Sent sample message [" + message + "]");
	}
	
}
