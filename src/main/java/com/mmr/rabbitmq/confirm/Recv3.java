package com.mmr.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * rabbitmq消息确认机制之confirm
 * 异步模式
 * 不管是普通模式还是批量模式，他们都是发完之后再确认，也就是说他们都是串行的。
 *
 */
public class Recv3 {
	
	//队列的名称
	private static final String QUEUE_NAME="test_queue_confirm3";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		Connection connection = ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("Recv[confirm3] msg:"+msg);
			}
		});
		
	}
}



















