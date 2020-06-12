package com.mmr.rabbitmq.tx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 事务机制
 * 
 * 生产者  发送数据(消息)
 * @author Administrator
 *
 */
public class Send {

	
	//交换机的名称
	private static final String QUEUE_NAME="test_queue_tx";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取一个连接
		Connection connection=ConnectionUtils.getConnection();
		
		//从连接中创建一个通道
		Channel channel=connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		//发送消息
		String msg="hello tx message";
		
		try {
			//lenovo JDBC中的DBUtil.TransBegin();开启事务（我们自行维护事务提交） 其实也就是把自动提交事务关了：conn.setAutoCommit(false);  
			channel.txSelect();//开启事务
			
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			
			//int x=1/0;//如果在这里发生异常：就会执行最下面的回滚操作，控制台会输出：send message txRollback，Recv1接收不到任何消息
			
			System.out.println("Send :"+msg);
			
			//如果把这一行给注释掉，并且上面也不会发生异常，那么Recv1会接收到消息么？不会
			channel.txCommit();//提交事务
			
		}catch(Exception e) {
			channel.txRollback();//回滚事务
			System.out.println("send message txRollback");
		}
		
		channel.close();
		connection.close();
		
		
		
	}
}









































