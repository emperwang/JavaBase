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
import java.util.ArrayList;
import java.util.HashMap;
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
//        updateAgeList();
//        selectById();
//        selectByIdWithMap();
//        selectiveUpdateSet();
//        selectiveUpdateTrim();
//        insertBatch();
//        batchDelete();
//        batchDelArray();
//        chooseSelect();
//        chooseSelect2();
        chooseSelect3();
    }

    public static void batchDelArray(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Integer[] arr = {1,2,3,4,5};
        mapper.batchDelArray(arr);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void batchDelete(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> list = new ArrayList<>();
        for(int i = 10; i < 10; i++){
            User user = new User();
            user.setId(i);
            list.add(user);
        }
        mapper.batchDeletes(list);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     *  查询 address = gc的记录
     */
    public static void chooseSelect(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setAddress("gd");
        List<User> users = mapper.chooseSelect(user);
        System.out.println(users.toString());
        sqlSession.close();
    }

    /**
     *  查询默认记录
     */
    public static void chooseSelect2(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        List<User> users = mapper.chooseSelect(user);
        System.out.println(users.toString());
        sqlSession.close();
    }

    /**
     * 查询年龄为200的记录
     */
    public static void chooseSelect3(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setAge(200);
        List<User> users = mapper.chooseSelect(user);
        System.out.println(users.toString());
        sqlSession.close();
    }


    public static void selectByIdWithMap(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, String> map = new HashMap<>(1);
        map.put("id","1");
        User user = mapper.selectByMap(map);
        log.info(user.toString());
        sqlSession.close();
    }

    public static void selectById(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById(1);
        log.info(user.toString());
        sqlSession.close();
    }

    public static void selectiveUpdateSet(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(1);
        user.setAddress("金星5555");
        mapper.updateSelectField(user);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectiveUpdateTrim(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(2);
        user.setAddress("火星bj");
        mapper.updateSelectFieldTrim(user);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertBatch(){
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> lists = new ArrayList<>();
        for (int i=0;i<10;i++){
            User user = new User();
            user.setAge(155);
            user.setName("洪七公 :"+i+"  徒弟");
            user.setAddress("叫花鸡");
            lists.add(user);
        }
        userMapper.batchInsert(lists);
        sqlSession.commit();
        sqlSession.close();
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
