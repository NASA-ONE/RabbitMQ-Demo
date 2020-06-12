package com.mmr.rabbitmq.confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

/**
 * rabbitmq消息确认机制之confirm串行 
 * 异步模式
 *
 */
public class Send3 {

	private static final String QUEUE_NAME="test_queue_confirm3";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		channel.confirmSelect();//将channel设置为confirm模式
		
		//未确认的消息的标识(也即是该消息的ID，它是唯一、从1开始递增的整数)
		final SortedSet<Long> confirmSet=Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		//通道添加监听（异步监听rabbitmq服务器的响应）
		channel.addConfirmListener(new ConfirmListener() {
			
			//没有问题的handleAck（rabbitmq成功接收到消息）
			public void handleAck(long deliveryTag,boolean multiple) throws IOException{//multiple 是否为多个，如果为多个就表示：到这个序列号之前的所有消息都已经得到了处理
				//System.out.println("++++++++++++++++++++++++++++++"+deliveryTag);
				if(multiple) {//从打印结果来看，返回的既有单条也有多条
					System.out.println("---handleAck---multiple");
					confirmSet.headSet(deliveryTag+1).clear(); //headSet(E e)//e之前的元素，不包括e
				}else {
					System.out.println("---handleAck---multiple false");
					confirmSet.remove(deliveryTag);//
				}
			}
			
			//有问题的handleNack（rabbitmq接收消息失败）
			//该方法的代码逻辑和上面一样(这么写只是为了测试下)不管成功还是失败都把消息ID从集合中移除掉，实际业务场景肯定不这么干，对于这种发送失败的
			//可以重新发送(eg:3秒之后再发，10分钟后再发)，比如我们商户开发者调用支付宝提供的支付接口时，当调用失败后，它会3秒之后再发，如果还失败，它会10秒之后再发......失败到一定次数之后就不再发送了。
			public void handleNack(long deliveryTag,boolean multiple) throws IOException{
				if(multiple) {
					System.out.println("---handleNack---multiple");
					confirmSet.headSet(deliveryTag+1).clear();
				}else {
					System.out.println("---handleNack---multiple false");
					confirmSet.remove(deliveryTag);
				}
			}
			
		});
		
		//发送消息
		String msg="sssssssss";
		
		while(true) {
			long seqNo=channel.getNextPublishSeqNo();//消息的ID，每次重新运行该类都会从1开始算起
			System.out.println("---------------------------------------------"+seqNo);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(seqNo);//使用集合存储该消息ID
		}
		
		
		
		
		
	}
}









































