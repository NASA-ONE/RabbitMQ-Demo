package com.mmr.rabbitmq.spring;
/**
 * 消费者
 * @author Administrator
 *
 */
public class MyConsumer {

	public void listen(String foo) {
		System.out.println("消费者："+foo);
	}
}
