package com.wk.start;

import com.wk.api.bean.User;
import com.wk.api.service.UserService;
import com.wk.service.UserServiceInvoke;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartMain {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");
        applicationContext.start();
        UserService service = (UserService)applicationContext.getBean("userService");
        User user = service.getById(1);
        System.out.println(user);
        System.in.read();
    }
}
