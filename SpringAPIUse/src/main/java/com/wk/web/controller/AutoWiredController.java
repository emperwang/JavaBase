package com.wk.web.controller;

import com.wk.web.service.AutoWiredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.inject.Inject;

@Controller
public class AutoWiredController {
    //@Qualifier("service")
    //@Autowired
    //@Resource(name = "autoWiredService")
    @Inject
    private AutoWiredService autoWiredService;

    @Override
    public String toString() {
        return "AutoWiredController{" +
                "servicel=" + autoWiredService +
                '}';
    }
}
