package com.wk.web.controller;

import com.wk.bean.UserBean;
import com.wk.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Autowired
    private UserService userService;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserBean> getAllUser(){
        List<UserBean> allUser = userService.getAllUser();

        return allUser;
    }
}
