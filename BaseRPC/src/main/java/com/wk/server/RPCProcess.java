package com.wk.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

/**
 * 具体处理每一个客户端发送过来的请求.
 * 1.从socket中读取方法,方法参数等对象.
 * 2.通过反射调用具体的方法
 * 3.把方法的处理结果返回给用户
 */
public class RPCProcess implements Runnable {
    private Socket socket;
    private final Object service;

    public RPCProcess(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        // 获取输入流,从源中读取对象到程序中
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            String methodName = inputStream.readUTF();
            System.out.println("server get Method name is :" + methodName);
            //得到参数类型
            Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
            System.out.println("Server get paramterTypes is :" + Arrays.toString(parameterTypes));
            //获取参数
            Object[] arguments = (Object[]) inputStream.readObject();
            System.out.println("arguments :" + Arrays.toString(arguments));
            // server处理请求, 进行响应
            try(ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
                Method method = service.getClass().getMethod(methodName, parameterTypes);
                Object result = method.invoke(service, arguments);
                System.out.println("service 处理的请求结果: " + result);
                outputStream.writeObject(result);
                outputStream.flush();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
