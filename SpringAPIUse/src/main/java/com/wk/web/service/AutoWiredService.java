package com.wk.web.service;

import com.wk.web.dao.AutowiredDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AutoWiredService {
    //@Autowired
    @Resource
    private AutowiredDao dao;

    
    @Override
    public String toString() {
        return "AutoWiredService{" +
                "dao=" + dao +
                '}';
    }
}
