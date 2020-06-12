package com.mmr.rabbitmq.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 工作队列
 * 
 * 生产者  发送数据(消息)
 * @author Administrator
 *
 */
public class Send {

	/*					|---C1
	 *   p-----Queue----|
	 * 					|---C2
	 */
	
	//队列的名称（告诉生产者往哪个队列放数据）
	private static final String QUEUE_NAME="test_work_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		
		//创建队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		for(int i=0;i<50;i++) {//连续发送50条数据
			String msg="hello "+i;
			System.out.println("[WQ] send:"+msg);
			//往指定队列里发送数据(消息)
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			
			Thread.sleep(i*20);
		}
		
		channel.close();
		connection.close();
		
		
		
	}
}
















