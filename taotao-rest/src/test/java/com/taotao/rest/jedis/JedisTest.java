package com.taotao.rest.jedis;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedisSingle(){
		//创建一个jedis对象
		Jedis jedis = new Jedis("192.168.10.59", 6379);
		//调用jedis对象方法，方法名称和redis命令一样
		jedis.set("key1", "jedis test");
		String key = jedis.get("key1");
		System.out.println(key);
		//关闭jedis
		jedis.close();
	}
	/**
	 * 使用连接池
	 */
	@Test
	public void testJedisPool(){
		//创建jedis连接池
		JedisPool jp = new JedisPool("192.168.10.59", 6379);
		//从连接池获取Jedis对象
		Jedis jedis = jp.getResource();
		String key = jedis.get("key1");
		System.out.println(key);
		jedis.close();
		jp.close();
	}
	/**
	 * 集群版
	 */
	public void testJedisCluster(){
		/*HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.10.59", 7001));
		nodes.add(new HostAndPort("192.168.10.59", 7002));
		nodes.add(new HostAndPort("192.168.10.59", 7003));
		nodes.add(new HostAndPort("192.168.10.59", 7004));
		nodes.add(new HostAndPort("192.168.10.59", 7005));
		JedisCluster jc = new JedisCluster(nodes);
		jc.set("cc", "123");
		jc.get("cc");*/
	}
	
	/**
	 * 单机版测试
	 * <p>Title: testSpringJedisSingle</p>
	 * <p>Description: </p>
	 */
	@Test
	public void testSpringJedisSingle() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisPool pool = (JedisPool) applicationContext.getBean("redisClinet");
		Jedis jedis = pool.getResource();
		String string = jedis.get("ss");
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	/**
	 * 集群版测试
	 */
	@Test
	public void testSpringJedisCluster() {
		/*ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster =  (JedisCluster) applicationContext.getBean("redisClient");
		String string = jedisCluster.get("key1");
		System.out.println(string);
		jedisCluster.close();*/
	}
}
