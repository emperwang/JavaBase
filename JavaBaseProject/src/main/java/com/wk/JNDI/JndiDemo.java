package com.wk.JNDI;

import lombok.extern.slf4j.Slf4j;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

@Slf4j
public class JndiDemo {
    /**
     *  绑定一个对象到  JNDI 服务上
     * @throws RemoteException
     * @throws NamingException
     */
    public void initPerson() throws RemoteException, NamingException {
        // 配置JNDI工厂和JNDI的url和端口
        LocateRegistry.createRegistry(3000);
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.rmi.registry.RegistryContextFactory");
        System.setProperty(Context.PROVIDER_URL,"rmi://localhost:3000");

        // 初始化
        InitialContext context = new InitialContext();
        // 创建一个对象
        Person person = new Person();
        person.setName("zc");
        person.setPassword("123456");

        // 绑定对象,名字为person,实例对象为第二个参数person
        context.bind("person",person);
        context.close();
    }

    /**
     *  通过JNDI 查找对象
     * @throws NamingException
     */
    public void findPerson() throws NamingException {
        // 因为前面已经将JNDI工厂和JNDI的url和端口已经添加到system对象中
        // 这里就不同再绑定了
        InitialContext context = new InitialContext();
        // 查找对象
        Person person = (Person) context.lookup("person");
        // 打印
        log.info("person: "+person);
        context.close();
    }

    public static void main(String[] args) throws NamingException, RemoteException {
        JndiDemo jndiDemo = new JndiDemo();
        jndiDemo.initPerson();
        jndiDemo.findPerson();
    }

}
