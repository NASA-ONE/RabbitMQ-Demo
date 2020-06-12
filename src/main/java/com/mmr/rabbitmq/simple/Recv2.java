package com.mmr.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 消费者获取消息
 * @author Administrator
 *
 */
public class Recv2 {
	private static final String QUEUE_NAME="test_simple_queue";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//创建频道
		Channel channel=connection.createChannel();
		
		//定义队列的消费者
		//老版本写法
		QueueingConsumer consumer=new QueueingConsumer(channel);
		//监听队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		while(true) {
			Delivery delivery=consumer.nextDelivery();
			String msgString =new String(delivery.getBody());
			System.out.println("[recv] msg:"+msgString);
		}
		
		
		
	}
}



