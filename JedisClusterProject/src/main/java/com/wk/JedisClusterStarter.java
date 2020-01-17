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
        expire("test1", 50000);
        get("test1");
        del("test1");

//        hset("hsetTest","hsetField","hsetValue");
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
                    System.out.println("set retry ......");
                    sleep(3);
                }
            }
        }
        return "";
    }

    public static String get(String key){
        boolean attempt = false;
        while (! attempt) {
            attempt = true;
            try {
                String get = jedisCluster.get(key);
                System.out.println("get return :" + get);
                return get;
            }catch (JedisConnectionException e){
                if (isRetry){
                    attempt = false;
                    System.out.println("get retry ......");
                    sleep(3);
                }
            }
        }
        return "";
    }

    public static long expire(String key, int expire) {
        boolean attempt = false;
        while (!attempt){
            attempt = true;
            try {
                Long expire1 = jedisCluster.expire(key, expire);
                System.out.println("expire return: "+expire1);
                return expire1;
            }catch (JedisConnectionException e){
                if (isRetry){
                    attempt = false;
                    System.out.println("expire retry ......");
                    sleep(2);
                }
            }
        }
        return 0;
    }

    public static long del(String key) {
        boolean attempt = false;
        while (!attempt){
            attempt = true;
            try {
                Long del = jedisCluster.del(key);
                System.out.println("del return :" + del);
                return del;
            }catch (JedisConnectionException e){
                if (isRetry) {
                    attempt = false;
                    System.out.println("del retry ......");
                    sleep(2);
                }
            }
        }
        return 0;
    }

    public static long hset(String key, String field, String value) {
        boolean attempt = false;
        while (!attempt){
            attempt = true;
            try {
                Long hset = jedisCluster.hset(key, field, value);
                System.out.println("hset retrun : "+ hset);
                return hset;
            }catch (JedisConnectionException e){
                if (isRetry) {
                    attempt = false;
                    System.out.println("hset retry ......");
                    sleep(2);
                }
            }
        }
        return 0;
    }

    private static void sleep(int seconds){
        try{
         Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
