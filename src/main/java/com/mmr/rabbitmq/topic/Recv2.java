package com.mmr.rabbitmq.topic;

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
	
	//队列的名称
	private static final String QUEUE_NAME="test_queue_topic_2";
	//交换机名称
	private static final String EXCHANGE_NAME="test_exchange_topic";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//创建频道
		final Channel channel=connection.createChannel();
		
		//队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		//绑定队列到交换机（转发器）
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "goods.#");//消费者2全部匹配（商品发布、修改、删除、查询的消息全部接收）
		
		channel.basicQos(1);//保证一次只分发一个  默认值为0
		
		//定义一个消费者     监听器监听到队列中有消息时，会触发handleDelivery(......)方法
		Consumer consumer=new DefaultConsumer(channel) {
			//获取消息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("[2] Recv msg:"+msg);
				try {
					Thread.sleep(2000);//模拟消费者业务处理的时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
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



















