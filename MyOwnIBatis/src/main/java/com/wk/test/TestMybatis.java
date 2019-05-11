package com.wk.test;

import com.wk.Bean.User;
import com.wk.Mapper.UserMapper;
import com.wk.SqlSession.MySqlSession;

public class TestMybatis {
    public static void main(String[] args) {
        MySqlSession mySqlSession = new MySqlSession();
        UserMapper mapper = mySqlSession.getMapper(UserMapper.class);
        User user = mapper.getById(2);
        System.out.println(user);
    }
}
