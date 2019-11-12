package com.wk.common_pool2.keyPool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

// 工厂方法
public class DbConnectionKeyFactory implements KeyedPooledObjectFactory<String,DbConnection2> {
    @Override
    public PooledObject<DbConnection2> makeObject(String key) throws Exception {
        DbConnection2 dbConnection2 = new DbConnection2(key);
        dbConnection2.setActive(true);
        return new DefaultPooledObject<>(dbConnection2);
    }

    @Override
    public void destroyObject(String key, PooledObject<DbConnection2> p) throws Exception {
        p.getObject().setActive(false);
    }

    @Override
    public boolean validateObject(String key, PooledObject<DbConnection2> p) {
        return p.getObject().isActive();
    }

    @Override
    public void activateObject(String key, PooledObject<DbConnection2> p) throws Exception {
        p.getObject().setActive(true);
    }

    @Override
    public void passivateObject(String key, PooledObject<DbConnection2> p) throws Exception {

    }
}
