/**
 *
 * @date：2018年6月8日-下午1:33:32
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.stip.net.common.pojo.SampleMessage;

/**
 *
 * @Title:
 * @date：2018年6月8日-下午1:33:32
 * @author：chenjunan
 *
 */
@Component
public class KafkaConsumer {
	
	@KafkaListener(topics = "logstashLog")
	public void processMessage(SampleMessage message) {
		System.out.println("Received sample message [" + message + "]");
	}
	
}
