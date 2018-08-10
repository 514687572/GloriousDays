/**
 *
 * @date：2018年6月1日-上午11:46:09
 * Copyright © 2018 514687572@qq.com All rights reserved.
 *
 */
package com.stip.net.common.service.transaction;

/**
 *
 * @Title:
 * @date：2018年6月1日-上午11:46:09
 * @author：chenjunan
 *
 */
public interface TransactionService {
	
	public void sendMessage(String correlationId, Object order) throws Exception;
	
	public String sendTransactionMessage(String queueName,String correlationId,Object order) throws Exception;

}
