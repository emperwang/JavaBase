package com.wk.controller;

import com.wk.annotation.Controller;
import com.wk.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class TestController {

    @RequestMapping("/add")
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("add user success !!!..");
    }
}
