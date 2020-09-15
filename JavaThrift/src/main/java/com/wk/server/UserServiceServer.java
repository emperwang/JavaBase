package com.wk.server;

import com.wk.thrift_demo.UserService;
import org.apache.thrift.TException;

public class UserServiceServer implements UserService.Iface {

    private final static String HName="huawei";

    @Override
    public String getName(int id) throws TException {
        System.out.println("received getName, id= " + id);
        return HName;
    }

    @Override
    public boolean isExist(String name) throws TException {
        System.out.println("receive isExist, name="+name);
        return HName.equalsIgnoreCase(name);
    }
}
