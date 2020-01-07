package com.wk;

import com.wk.entity.User;
import com.wk.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:04 2019/12/31
 * @modifier:
 */
public class MainStarter {
    private static Logger log = Logger.getLogger(MainStarter.class);
    private static Reader reader;
    private static SqlSessionFactory sessionFactory;
    static {
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
//        selectAll();
        updateAgeList();
    }

    public static void updateAgeList(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            user.setAge(200);
        }
        userMapper.updateAgeList(users);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectAll(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectAll();
        log.info(users.toString());
        sqlSession.close();
    }
}
