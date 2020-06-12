package com.mmr.rabbitmq.tx;

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
 * 消费者1 
 * @author Administrator
 *
 */
public class Recv1 {
	
	//队列的名称
	private static final String QUEUE_NAME="test_queue_tx";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//创建频道
		final Channel channel=connection.createChannel();
		
		//队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("[1] Recv msg:"+msg);
			}
		});
		
	}
}



















