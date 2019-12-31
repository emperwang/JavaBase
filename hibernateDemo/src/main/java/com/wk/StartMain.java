package com.wk;

import com.wk.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:57 2019/12/31
 * @modifier:
 */
public class StartMain {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    static {
        configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }


    public static void main(String[] args) {
        insertRec();
        getAll();
    }

    public static void insertRec(){
        User user = new User();
        user.setAge(44444);
        user.setName("niangniang");
        user.setAddress("bj-gd");
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    public static void getAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, 1);
        System.out.println(user.toString());
        transaction.commit();

        session.close();
    }
}
