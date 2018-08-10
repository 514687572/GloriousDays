/**
 *
 * @date：2018年6月8日-下午3:18:17
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.common.service.kafka;

import com.stip.net.common.pojo.SampleMessage;

/**
 *
 * @Title:
 * @date：2018年6月8日-下午3:18:17
 * @author：chenjunan
 *
 */
public interface KafkaProducerService {
	public void send(SampleMessage message);
}
