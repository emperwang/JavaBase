package com.wk.web.controller;

import com.wk.entity.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/first")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @GET
    @Path("/str.do")
    @Produces(MediaType.APPLICATION_JSON)
    public String str(){
        return "Hello Jersey";
    }
    @GET
    @Path("/user.do")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(){
        User user = new User();
        user.setName("zhangsan");
        user.setAge(10);

        return user;
    }
}
