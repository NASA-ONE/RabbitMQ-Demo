package com.mmr.rabbitmq.ps;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 发布/订阅模式
 * 
 * 生产者  发送数据(消息)
 * @author Administrator
 *
 */
public class Send {

	
	//交换机的名称
	private static final String EXCHANGE_NAME="test_exchange_fanout";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		//声明交换机(就像创建队列声明那样)
		
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//fanout表示分发的意思，后面再讲交换机的其他类型
		
		//发送消息
		String msg="hello ps";
		
		
		/*
		 * 第一个参数不为空，第二个参数为空，因为在该种模式下消费者只能往交换机中发送数据，
		 * lenovo简单队列中的往指定队列里发送消息的代码：
		 * channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		 * 这里只能往队列中发送消息，因为简单队列中还没有交换机。
		 * 下面这个方法和简单队列中的这个方法一样的。
		 */
		
		channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
		
		System.out.println("Send :"+msg);
		
		channel.close();
		connection.close();
		
		
		
	}
}









































