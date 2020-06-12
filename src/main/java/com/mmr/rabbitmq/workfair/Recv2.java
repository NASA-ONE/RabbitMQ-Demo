package com.mmr.rabbitmq.workfair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * 消费者2
 * @author Administrator
 *
 */
public class Recv2 {
	
	//队列的名称（告诉消费者往哪个队列取数据）
	private static final String QUEUE_NAME="test_work_queue";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//创建频道
		final Channel channel=connection.createChannel();
		
		//队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicQos(1);//保证一次只分发一个   默认值为0
		
		//定义一个消费者     监听器监听到队列中有消息时，会触发handleDelivery(......)方法
		Consumer consumer=new DefaultConsumer(channel) {
			//获取消息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("[2] Recv msg:"+msg);
				try {
					Thread.sleep(1000);//比消费者1延时低，这里设置为1秒，消费者1设置的是2秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					System.out.println("[2] done");
					//当前消息处理完之后，手动回执一个消息
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		
		//消费者监听队列QUEUE_NAME（就相当于添加个监听器）
		boolean autoAck=false;//关闭消息自动应答，改为false
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
	}
}



















