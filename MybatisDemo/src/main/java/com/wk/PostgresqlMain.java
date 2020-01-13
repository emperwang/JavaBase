package com.wk;


import com.wk.entity.User;
import com.wk.pmapper.UserMapper;
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
 * @time: 10:32 2020/1/7
 * @modifier:
 */
public class PostgresqlMain {
    private static Logger log = Logger.getLogger(PostgresqlMain.class);
    private static Reader reader;
    private static SqlSessionFactory sessionFactory;
    static {
        try {
            reader = Resources.getResourceAsReader("mybatis-postgresql.xml");
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

    public static void selectAll(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.getAll();
        log.info(users.toString());
        sqlSession.close();
    }

    public static void getAllId(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<Integer> allId = userMapper.getAllId();
        log.info(allId.toString());
        sqlSession.close();
    }

    public static void updates(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.getAll();
        for (User user : all) {
            user.setAge(200);
        }
        mapper.updateAges(all);
        sqlSession.commit();
        sqlSession.close();
    }


    public static void main(String[] args) {
//        selectAll();
        getAllId();
//        updates();
    }
}
