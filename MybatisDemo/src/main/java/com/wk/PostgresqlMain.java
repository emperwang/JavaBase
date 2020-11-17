package com.wk;


import com.wk.entity.User;
import com.wk.pmapper.UserMapper;
import com.wk.util.PostgresqlUtil;
import lombok.Getter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 10:32 2020/1/7
 * @modifier:
 */
@Getter
public class PostgresqlMain {
    private static Logger log = Logger.getLogger(PostgresqlMain.class);
    private static SqlSessionFactory sessionFactory = PostgresqlUtil.getSessionFactory();

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
        for (int i = 0; i < all.size(); i++) {
            all.get(i).setAge(200);
            all.get(i).setName("zh-" + i);
        }
        mapper.batchUpdate(all);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void updates2(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.getAll();
        for (int i = 0; i < all.size(); i++) {
            if (i % 2 == 0) {
                all.get(i).setAge(null);
            }else{
                all.get(i).setAge(2000);
            }
            all.get(i).setName("ch-" + i);
        }
        mapper.batchUdates(all);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void batchInsert(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        final User user = new User();
        user.setName("k1");
        user.setAge(20);
        final User user1 = new User();
        user1.setName("k2");
        user1.setAge(20);
        final User user2 = new User();
        user2.setName("k3");
        user2.setAge(20);
        final List<User> users = Arrays.asList(user, user1, user2);
        mapper.batchInsert(users);
        System.out.println("batch insert");
        System.out.println(users.toString());
        sqlSession.commit();
        sqlSession.close();
    }
    public static void getSeq() {
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        final int seq = mapper.getSeq();
        System.out.println(seq);
        sqlSession.close();
    }

    public static void  getSeqOnce(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        final int seq = mapper.getSeq();
        System.out.println(seq);
        sqlSession.close();
    }

    public static void getSeq2() {
//        for (int i =0; i< 10; i++) {
//            getSeqOnce();
//        }
        final List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 6);
        integers.stream().forEach(tt -> {
            System.out.println(tt);
            getSeqOnce();
        });
    }

    public static void main(String[] args) {
//        selectAll();
//        getAllId();
//            updates();
//        batchInsert();
//        updates2();
//        getSeq();
        getSeq2();
    }
}
