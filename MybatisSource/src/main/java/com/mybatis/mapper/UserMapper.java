package com.mybatis.mapper;

import com.mybatis.bean.User;

import java.util.List;

public interface UserMapper {

    List<User> findAll();

    User findById(Integer id);
}
