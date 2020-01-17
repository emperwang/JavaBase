package com.wk;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashSet;
import java.util.Set;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 10:36 2020/1/17
 * @modifier:
 */
public class JedisClusterStarter {
    private static JedisCluster jedisCluster;
    private static boolean isRetry = true;
    static {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.72.18",9001));
        nodes.add(new HostAndPort("192.168.72.18",9002));
        nodes.add(new HostAndPort("192.168.72.18",9003));
        nodes.add(new HostAndPort("192.168.72.18",9004));
        nodes.add(new HostAndPort("192.168.72.18",9005));
        nodes.add(new HostAndPort("192.168.72.18",9006));
        jedisCluster = new JedisCluster(nodes);
    }

    public static void main(String[] args) {
        set("test1", "testValue");
    }


    public static String set(String key, String value){
        boolean attempt = false;
        while (! attempt) {
            attempt = true;
            try {
                String set = jedisCluster.set(key, value);
                System.out.println("set return :" + set);
                return set;
            }catch (JedisConnectionException e){
                if (isRetry){
                    attempt = false;
                    System.out.println("retry ......");
                    sleep(3);
                }
            }
        }
        return "";
    }


    private static void sleep(int seconds){
        try{
         Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
