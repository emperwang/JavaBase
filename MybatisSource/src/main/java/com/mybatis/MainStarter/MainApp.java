package com.mybatis.MainStarter;

import com.mybatis.Util.SqlSessionFactoryUtil;
import com.mybatis.bean.User;
import com.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 *  1. 配置文件配置流程
 *  2. 接口和类的对应关系
 *  3. 一个接口执行的流程(接口找到对应的sql语句)
 *  4. 参数的配置
 *  5. 连接池
 *  6. 事务管理
 *  7. 结果如何映射到javabean中
 */
public class MainApp {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<User> all = userMapper.findAll();
        for (User user : all) {
            System.out.println(user);
        }

        User user = userMapper.findById(1);
        System.out.println(user);
    }
}
