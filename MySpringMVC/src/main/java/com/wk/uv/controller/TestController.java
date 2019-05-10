package com.wk.uv.controller;

import com.wk.base.annotation.Autowired;
import com.wk.base.annotation.Controller;
import com.wk.base.annotation.RequestMapping;
import com.wk.base.annotation.ResponseBody;
import com.wk.uv.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class TestController {

    /*@Autowired
    private UserService userService;*/

    @RequestMapping("/add")
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println("in controller service is:" + userService);
        //String result = userService.addUser();
        response.getWriter().write( " ..add user success !!!..");
    }
}
