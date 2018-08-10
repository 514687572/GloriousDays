package com.stip.net.rabbitmq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ErrorHandler;

import com.stip.net.rabbitmq.utils.AttactMessageFilter;
import com.stip.net.rabbitmq.utils.MessageFatalExceptionStrategy;
import com.stip.net.rabbitmq.utils.RabbitmqProps;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAutoConfiguration
@EnableRabbit
public class RabbitConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);
	
	public static final String ROUNTING_KEY_PREFIX="stip.transaction";
	
	public static final String ORDER_SAVE_ROUTING_KEY="transaction.transfer";
	
	static {
		AttactMessageFilter.init(Arrays.asList(new String[]{ORDER_SAVE_ROUTING_KEY}));
	}
	
	@Autowired
	private RabbitmqProps rabbitmqProps;

	/**
	 * 
	 * rabbitMq连接
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.rabbitmq")   
    public ConnectionFactory connectionFactory() {  
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  
        //防止信道缓存不够造成消息丢失，官方推荐100可完全避免此丢失消息情况
        connectionFactory.setChannelCacheSize(100);
        connectionFactory.setAddresses(rabbitmqProps.getAddresses());  
        connectionFactory.setUsername(rabbitmqProps.getUsername());  
        connectionFactory.setPassword(rabbitmqProps.getPassword());  
        connectionFactory.setVirtualHost("/"); 
        //开启确认机制，可监听消息是否到达交换机
        connectionFactory.setPublisherConfirms(rabbitmqProps.isPublisherConfirms()); 
        //mandatory，不可路由时回调
        //connectionFactory.setPublisherReturns(true);
        
        return connectionFactory;  
    }   
	
	/**
	 * 
	 * 用于恢复交换机，队列
	 * 
	 * @return
	 */
	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}
	
    /**
     * 
     * rabbitTemplate必须是prototype
     *   
     * @return
     */
    @Bean  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public RabbitTemplate rabbitTemplate() {  
        RabbitTemplate template = new RabbitTemplate(connectionFactory()); 
        template.setChannelTransacted(true);
        
        return template;  
    }  
    
    @Bean  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTransactionManager transactionManager() {
    	RabbitTransactionManager rabbitTransactionManager=new RabbitTransactionManager(connectionFactory());
    	
		return rabbitTransactionManager;
    }
    
    /**
     * 
     * transactionTemplate必须是prototype
     *   
     * @return
     */
    @Bean  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public TransactionTemplate transactionTemplate() {  
    	TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager());
    	
    	return transactionTemplate;  
    }  
    
    /**  
     * 
     * 业务交换机tipic方式
     *   
     */  
    @Bean
    public TopicExchange bussinessExchange(){
    	return new TopicExchange(rabbitmqProps.getExchange());  
    }
    
    /**
     * 
     * 死信交换机topic方式
     * 
     * @return
     */
    @Bean  
    public TopicExchange dlxExchange() {  
        return new TopicExchange("dlxExchange");  
    }

    /**
     * 
     * 业务队列
     * 
     * @return
     */
    @Bean  
    public Queue queue() {  
    	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("x-dead-letter-exchange", "dlxExchange");
    	params.put("x-message-ttl", 6000);
        return new Queue(rabbitmqProps.getQueueName(), true, false, false,params); 
    }  
    
    /**
     * 
     * 死信队列
     * 
     * @return
     */
    @Bean
    public Queue dlxQueue(){
    	Map<String,Object> params=new HashMap<String,Object>();
    	Queue queue=new Queue("dlxQueue", true, false, false,params);
    	return queue;
    }
    
    /**
     * 
     * 订单业务绑定
     * 
     * @return
     */
    @Bean  
    public Binding binding() {  
        return BindingBuilder.bind(queue()).to(bussinessExchange()).with(rabbitmqProps.getKeys().get("orderRouting"));  
    }  

    /**
     * 
     * 死信队列绑定
     * 
     * @return
     */
    @Bean
    public Binding dlxBinding() {	
    	//topic的方式
    	return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(rabbitmqProps.getKeys().get("orderRouting"));
    }
    
    /**
     * 
     * 使用@RabbitListener必须用该对象，不建议手动ack
     * 
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        //尽管这里设置了可重入队列，但是消费端抛出AmqpRejectAndDontRequeueException也可使其不可重入队列
        //消费端通过控制AmqpRejectAndDontRequeueException来分情况进行是否可重入队列而不是一味重发是很好的方案
        //一般不可重入队列后，可放入死信队列，然后集中分情况进行处理
        factory.setDefaultRequeueRejected(true);
        //自动ack
        //factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置致命错误，使其不进入死信
        factory.setErrorHandler(errorHandler());
        factory.setTransactionManager(transactionManager());
        
        return factory;
    }
    
    /**
     * 
     * 消费端致命错误不进入死信重发
     * 
     * @return
     */
    @Bean
	public ErrorHandler errorHandler() {
		return new ConditionalRejectingErrorHandler(new MessageFatalExceptionStrategy());
	}
    
}
