package com.wk;

import com.wk.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 16:52 2020/1/8
 * @modifier:
 */
public class SQLMain {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    static {
        configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     *  查询所有
     */
    public static void getAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select * from User";
        Query<User> nativeQuery = session.createNativeQuery(sql, User.class);
        List<User> list = nativeQuery.list();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    /**
     *   条件查询
     */
    public static void query2(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select * from User where id = :ids";
        Query<User> nativeQuery = session.createNativeQuery(sql, User.class);
        nativeQuery.setParameter("ids",20);
        List<User> list = nativeQuery.list();
        System.out.println(list);
//        User user = nativeQuery.uniqueResult();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }


    public static void main(String[] args) {
//        getAll();
        query2();
    }
}
