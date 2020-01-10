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
 * @time: 15:30 2020/1/10
 * @modifier:
 */
public class NamedQueryMain {
    private static SessionFactory sessionFactory;
    private static Configuration configuration;
    static {
        configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     * named hql  查询所有
     */
    public static void HQL_getAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query hql_get_all_user = session.getNamedQuery("HQL_GET_ALL_USER");
        List<User> list = hql_get_all_user.getResultList();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  named hql  根据条件查询
     */
    public static void HQL_getById(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("HQL_GET_BY_ID");
        query.setParameter("id",1);
        User o = (User) query.uniqueResult();
        System.out.println(o.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  named sql  查询所有
     */
    public static void SQL_getAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedNativeQuery("SQL_GET_ALL");
        List<User> list = (List<User>)query.list();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  named sql  条件查询
     */
    public static void SQL_getById(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedNativeQuery("SQL_GET_BY_ID");
        query.setParameter("id", 3);
        Object o = query.uniqueResult();
        System.out.println(o.toString());

        transaction.commit();
        session.close();
    }

    public static void na_hql_get_all(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("NA_HQL_GET_ALL");
        List list = query.getResultList();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    public static void na_hql_get_by_id(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("NA_HQL_GET_BY_ID");
        query.setParameter("id", 3);
        Object o = query.uniqueResult();
        System.out.println(o.toString());

        transaction.commit();
        session.close();
    }

    public static void na_sql_get_all(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedNativeQuery("NA_SQL_GET_ALL");
        List list = query.getResultList();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    public static void na_sql_get_by_id(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedNativeQuery("NA_SQL_GET_BY_ID");
        query.setParameter("id", 3);
        Object o = query.uniqueResult();
        System.out.println(o.toString());

        transaction.commit();
        session.close();
    }


    public static void main(String[] args) {
//        HQL_getAll();
//        HQL_getById();
//        SQL_getAll();
//        SQL_getById();
//        na_hql_get_all();
//        na_hql_get_by_id();
        na_sql_get_all();
        System.out.println("------------------------");
        na_sql_get_by_id();
    }
}
