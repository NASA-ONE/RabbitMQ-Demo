package com.mmr.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * topic主题模式
 * 
 * 生产者  发送数据(消息)
 * @author Administrator
 *
 */
public class Send {

	
	//交换机的名称
	private static final String EXCHANGE_NAME="test_exchange_topic";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");//类型为topic
		
		//发送消息
		String msg="商品......";
		
		//模拟商品发布，删除，修改，查询。
		//googs.add表示商品发布，goods.delete表示商品删除，goods.update表示商品修改，goods.select表示商品查询
		channel.basicPublish(EXCHANGE_NAME, "goods.add", null, msg.getBytes());
		
		System.out.println("Send :"+msg);
		
		channel.close();
		connection.close();
		
		
		
	}
}









































