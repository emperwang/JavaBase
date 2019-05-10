package com.wk.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Arrays;

/**
 *  1. 构建一个socket, 连接远程服务
 *  2. 向远程服务发送数据(方法名和参数)
 *  3. 接收远程服务响应的数据
 * @param <T>
 */
public class RpcProxyClient<T> {
    public T proxyClient(Class<T> interfaceClass, final String host, final int port){
        if(interfaceClass == null){
            throw new IllegalArgumentException("Interface class can not be null");
        }
        //jdk动态代理约束,只能实现对接口的代理
        if (!interfaceClass.isInterface()){
            throw new IllegalArgumentException("The "+interfaceClass.getName()+" must be interface");
        }
        if (host == null || host.length() ==0){
            throw new IllegalArgumentException("host == null");
        }
        if (port <= 0 || port >= 65535){
            throw new IllegalArgumentException("Invalid port "+port);
        }
        System.out.println("get Remote service "+interfaceClass.getName() + " from server"+host+":"+port);
        //JDK 动态代理
        T proxy = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            //invoke 方法本意是对目标方法的增强,在这里用于发送RPC请求和接收响应
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //
                Socket socket = new Socket(host, port);
                try(ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
                    //发送请求,实体分别是:方法名, 方法参数类型,方法参数
                    oos.writeUTF(method.getName());
                    oos.writeObject(method.getParameterTypes());
                    oos.writeObject(args);
                    System.out.println("methos name is:"+method.getName()+" method's paramterTypes :"+ Arrays.toString(method.getParameterTypes()) +
                    " arguments is :"+Arrays.toString(args));
                    //读取远程调用的返回结果
                    try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())){
                        Object result = ois.readObject();
                        if (result instanceof Throwable){
                            throw (Throwable)result;
                        }
                        System.out.println("The result is :"+result);
                        return result;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        });
        return proxy;
    }
}
