package com.wk;

import com.wk.entity.User;
import com.wk.entity.UserWrapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

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
    public static void queryAll2(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select * from userh";
        List<User> list = session.createNativeQuery(sql, User.class).getResultList();
        System.out.println(list.toString());
        transaction.commit();
        session.close();
    }

    /**
     *  查询指定的字段
     */
    public static void queryWithCusFields(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select id, name from userh";
        NativeQuery<Object[]> query = session.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        for (Object[] objects : resultList) {
            System.out.println("id :"+objects[0]+", name:"+objects[1]);
            System.out.println("----------------------------------");
        }

        transaction.commit();
        session.close();
    }

    public static void queryWithCusFields2(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select id, name from userh";
        // 指定封装类
        NativeQuery<UserWrapper> query = session.createNativeQuery(sql)
                                    .addEntity(UserWrapper.class);
        List<UserWrapper> resultList = query.getResultList();
        for (UserWrapper objects : resultList) {
            System.out.println(objects.toString());
            System.out.println("*********************************");
        }

        transaction.commit();
        session.close();
    }


    /**
     *  指定查询字段的  类型
     */
    public static void queryCusFieldWithScalar(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select id, name from userh";
        NativeQuery<Object[]> query = session.createNativeQuery(sql)
                                    .addScalar("id", IntegerType.INSTANCE)
                                    .addScalar("name", StringType.INSTANCE);

        List<Object[]> resultList = query.getResultList();
        for (Object[] objects : resultList) {
            System.out.println("id :"+objects[0]+", name:"+objects[1]);
            System.out.println("----------------------------------");
        }

        transaction.commit();
        session.close();
    }

    /**
     *   条件查询
     */
    public static void query2(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "select * from userh where id = :ids";
        Query<User> nativeQuery = session.createNativeQuery(sql, User.class).setParameter("ids","20");
        User user = nativeQuery.uniqueResult();
        System.out.println(user.toString());

        transaction.commit();
        session.close();
    }

    public static void main(String[] args) {
//        getAll();
//        query2();
//        queryAll2();
//        queryWithCusFields();
        queryWithCusFields2();
//        queryCusFieldWithScalar();
    }
}
