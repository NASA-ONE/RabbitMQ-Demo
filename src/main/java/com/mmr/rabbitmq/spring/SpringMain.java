package com.mmr.rabbitmq.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 生产者
 * @author Administrator
 *
 */
public class SpringMain {

	public static void main(String[] args) throws InterruptedException {
		AbstractApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:context.xml");
		//RabbitMQ模板(template是什么，它封装了一些API)
		RabbitTemplate template=ctx.getBean(RabbitTemplate.class);
		//发送消息
		template.convertAndSend("hello,world");
		Thread.sleep(1000);
		ctx.destroy();//容器销毁   如果我把spring-rabbit的版本改成1.7.5，这个destroy()方法的版本就不过时了
	}
}






