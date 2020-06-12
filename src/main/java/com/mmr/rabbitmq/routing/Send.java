package com.mmr.rabbitmq.routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Routing路由模式
 * 
 * 生产者  发送数据(消息)
 * @author Administrator
 *
 */
public class Send {

	
	//交换机的名称
	private static final String EXCHANGE_NAME="test_exchange_direct";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");//类型为direct：处理路由键
		
		//发送消息
		String msg="hello direct";
		
		String routingKey="info";//路由键分别为error、info、warning时，做测试
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
		
		System.out.println("Send :"+msg);
		
		channel.close();
		connection.close();
		
		
		
	}
}









































