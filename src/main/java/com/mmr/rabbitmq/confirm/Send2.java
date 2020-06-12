package com.mmr.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * rabbitmq消息确认机制之confirm串行 
 * 批量模式
 *
 */
public class Send2 {

	//交换机的名称
	private static final String QUEUE_NAME="test_queue_confirm1";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		//注意，如果使用了事物机制，那么就不能再这设置为confirm模式的
		channel.confirmSelect();//将channel设置为confirm模式
		
		//发送消息
		String msg="hello confirm message";
		
		//批量发送10条消息
		for(int i=0;i<10;i++) {
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		
		//批量发送完之后再确认（等待所有的消息都发送完）
		if(!channel.waitForConfirms()) {//如果生产者没有收到消息确认，那么这个消息就发送失败了
			System.out.println("message send failed");//发送失败
		}else {
			System.out.println("message send ok");//发送成功
		}
		
		channel.close();
		connection.close();
		
		
		
	}
}









































