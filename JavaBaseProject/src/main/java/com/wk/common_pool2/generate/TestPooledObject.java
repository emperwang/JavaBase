package com.wk.common_pool2.generate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

@Slf4j
public class TestPooledObject {
    public static void main(String[] args) {
        //genericObjectPool_demo();
        softReferenceObjectPool_demo();
    }
    // softReferencePool
    public static void softReferenceObjectPool_demo(){
        DbConnectFactory dbConnectFactory = new DbConnectFactory();

        SoftReferenceObjectPool<DBConnection> pool = new SoftReferenceObjectPool<DBConnection>(dbConnectFactory);
        DBConnection connection = null;
        try {
            connection = pool.borrowObject();
            int numActive = pool.getNumActive();
            int numIdle = pool.getNumIdle();
            log.info("connection ={},numActive={},numIdle={}",connection.toString(),numActive,numIdle);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    pool.returnObject(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 一般通用的 池
    public static void genericObjectPool_demo() {
        DbConnectFactory dbConnectFactory = new DbConnectFactory();
        GenericObjectPoolConfig<DBConnection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(20);
        poolConfig.setMaxTotal(100);
        poolConfig.setMinIdle(10);
        poolConfig.setMaxWaitMillis(5000);
        poolConfig.setTimeBetweenEvictionRunsMillis(500000);
        poolConfig.setNumTestsPerEvictionRun(5);

        GenericObjectPool<DBConnection> pool = new GenericObjectPool<DBConnection>(dbConnectFactory, poolConfig);
        DBConnection dbConnection = null;
        try{
            dbConnection = pool.borrowObject();
            int numActive = pool.getNumActive();
            long createdCount = pool.getCreatedCount();
        log.info("dbConnection : {},numActive={},createdCount={}", dbConnection.toString(),numActive,createdCount);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (dbConnection != null){
                pool.returnObject(dbConnection);
            }
        }
    }
}
