package com.wk.Service;

import com.wk.api.bean.User;
import com.wk.api.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getById(Integer id) {
        User user = new User(1, "bj", "1", "wk", "110", "true");
        return user;
    }
}
