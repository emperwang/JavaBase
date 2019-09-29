package com.wk.web.service.impl;

import com.wk.bean.UserBean;
import com.wk.web.mapper.UserBeanMapper;
import com.wk.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImple implements UserService{

    @Autowired
    private UserBeanMapper beanMapper;

    @Override
    public List<UserBean> getAllUser() {
        List<UserBean> userBeans = beanMapper.selectByExample(null);
        return userBeans;
    }
}
