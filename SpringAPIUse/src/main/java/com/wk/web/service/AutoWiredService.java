package com.wk.web.service;

import com.wk.web.dao.AutowiredDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoWiredService {
    @Autowired
    private AutowiredDao dao;

    @Override
    public String toString() {
        return "AutoWiredService{" +
                "dao=" + dao +
                '}';
    }
}
