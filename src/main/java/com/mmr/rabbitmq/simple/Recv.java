package com.mmr.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * 消费者  获取数据(消息)
 * @author Administrator
 *
 */
public class Recv {
	//队列的名称（告诉消费者往哪个队列取数据）
	private static final String QUEUE_NAME="test_simple_queue";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//创建频道
		Channel channel=connection.createChannel();
		
		
		//-------------老版本写法开始-------------------
		//定义队列的消费者
		//老版本写法(虽然已经是过期了的，但是还能用，代码能正常跑，能从队列中获取到数据)
//		QueueingConsumer consumer=new QueueingConsumer(channel);
//		//监听队列
//		channel.basicConsume(QUEUE_NAME, true, consumer);
//	
//		while(true) {
//			
//			Delivery delivery=consumer.nextDelivery();
//			String msgString =new String(delivery.getBody());
//			System.out.println("[recv] msg:"+msgString);
//		}
		
		//-------------老版本写法结束-------------------
		
		
		//-------------新版本写法开始：不再使用while了，用监听器-------------------
		//队列声明（老师说，作为消费者，不写这行队列声明也行，但为了保险起见，还是写这行代码吧）
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		//定义消费者     监听器监听到队列中有消息时，会触发handleDelivery(......)方法
		DefaultConsumer consumer=new DefaultConsumer(channel) {
			//获取消息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("new api recv:"+msg);
			}
		};
		
		//消费者监听队列QUEUE_NAME（就相当于添加个监听器）
		channel.basicConsume(QUEUE_NAME, true, consumer);
		//-------------新版本写法结束-------------------
		
	}
}



