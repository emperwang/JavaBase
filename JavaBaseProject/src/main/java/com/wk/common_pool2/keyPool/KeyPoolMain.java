package com.wk.common_pool2.keyPool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

@Slf4j
public class KeyPoolMain {
    public static void main(String[] args) {
        genericObjectKeyPoolConfig();
    }

    // genericObjectkeyedPool
    public static void genericObjectKeyPoolConfig(){
        GenericKeyedObjectPoolConfig<DbConnection2> config =
                                        new GenericKeyedObjectPoolConfig<>();
        config.setMaxIdlePerKey(20);
        config.setMaxTotal(500);
        config.setMinIdlePerKey(10);
        config.setMaxTotalPerKey(50);
        DbConnectionKeyFactory keyFactory = new DbConnectionKeyFactory();

        GenericKeyedObjectPool<String, DbConnection2> objectPool =
                                        new GenericKeyedObjectPool<>(keyFactory, config);
        DbConnection2 connection1 = null;
        DbConnection2 connection2 = null;
        try {
            connection1 = objectPool.borrowObject("db1");
            connection2 = objectPool.borrowObject("db2");
            log.info("connection1 = {},connection2={}",connection1.toString(),connection2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection1 != null){
                objectPool.returnObject(connection1.getUrl(),connection1);
            }
            if (connection2 != null){
                objectPool.returnObject(connection2.getUrl(),connection2);
            }
        }
    }
}
