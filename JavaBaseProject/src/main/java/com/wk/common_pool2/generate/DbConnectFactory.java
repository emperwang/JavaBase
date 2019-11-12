package com.wk.common_pool2.generate;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

// 对象工厂类
public class DbConnectFactory implements PooledObjectFactory<DBConnection> {
    @Override
    public PooledObject<DBConnection> makeObject() throws Exception {
        DBConnection dbConnection = new DBConnection(true);
        // 创建一个包装对象
        return new DefaultPooledObject<>(dbConnection);
    }

    @Override
    public void destroyObject(PooledObject<DBConnection> p) throws Exception {
        p.getObject().setActive(false);
    }

    @Override
    public boolean validateObject(PooledObject<DBConnection> p) {
        return  p.getObject().isActive();
    }

    @Override
    public void activateObject(PooledObject<DBConnection> p) throws Exception {
        p.getObject().setActive(true);
    }

    @Override
    public void passivateObject(PooledObject<DBConnection> p) throws Exception {

    }
}
