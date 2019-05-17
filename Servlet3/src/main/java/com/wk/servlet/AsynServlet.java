package com.wk.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// asyncSupported表示支持异步
@WebServlet(value = "/async",asyncSupported = true)
public class AsynServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("主线程开始:"+Thread.currentThread().getName()+":Start Time:"+System.currentTimeMillis());
        //1.支持异步
        //2.开启异步
        AsyncContext asyncContext = req.startAsync();
        //3.异步执行
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                System.out.println("副线程开始:"+Thread.currentThread().getName()+":Start Time:"+System.currentTimeMillis());
                sayHello();
                //4.异步执行完成后,通知完成
                asyncContext.complete();
                //5. 获取响应
                ServletResponse response = asyncContext.getResponse();
                try {
                    //6. 响应结果
                    response.getWriter().write("async complete.........");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("副线程结束:"+Thread.currentThread().getName()+": End Time:"+System.currentTimeMillis());
            }

        });
        System.out.println("主线程结束:"+Thread.currentThread().getName()+": End Time:"+System.currentTimeMillis());
    }

    public void sayHello(){
        try {
            System.out.println("The task is processing.....");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
