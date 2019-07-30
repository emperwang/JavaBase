package com.mybatis.MainStarter;

import com.mybatis.Util.SqlSessionFactoryUtil;
import com.mybatis.bean.User;
import com.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        
        List<User> all = userMapper.findAll();
        for (User user : all) {
            System.out.println(user);
        }

    }
}
