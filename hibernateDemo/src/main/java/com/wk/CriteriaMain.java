package com.wk;

import com.wk.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 16:52 2020/1/8
 * @modifier:
 */
public class CriteriaMain {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    static {
        configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     *  查询所有
     */
    public static void queryAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        Query<User> query = session.createQuery(criteria);
        List<User> lists = query.getResultList();
        System.out.println(lists.toString());

        transaction.commit();
        session.close();
    }

    public static void main(String[] args) {
        queryAll();
    }
}
