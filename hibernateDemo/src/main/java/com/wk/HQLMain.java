package com.wk;

import com.wk.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 16:52 2020/1/8
 * @modifier:
 */
public class HQLMain {
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
        String hql = "from User";
        Query query = session.createQuery(hql);
        List<User> list = query.list();

        System.out.println(list.toString());
        transaction.commit();
        session.close();
    }

    /**
     *  条件查询    使用 '?' 占位符
     */
    public static void queryWithcriteria(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from User where id = ?";
        Query query = session.createQuery(hql);
        query.setParameter(0,2);
        User user = (User) query.uniqueResult();
        System.out.println(user.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  条件查询  使用 ':name' 占位符
     */
    public static void queryWithNameCriteria(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from User where id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id",1);
        User user = (User) query.uniqueResult();
        System.out.println(user.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  分页查询
     */
    public static void queryWithLimit(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from User";
        Query query = session.createQuery(hql);
        query.setFirstResult(10);  // 设置查询其实位置
        query.setMaxResults(2);  // 设置一次查询多少条
        List<User> list = query.list();
        System.out.println(list.toString());

        transaction.commit();
        session.close();
    }

    /**
     *  查询一条记录
     */
    public static void insert(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = new User();
        user.setAddress("川蜀");
        user.setName("刘备");
        user.setAge(19);
        session.save(user);

        transaction.commit();
        session.close();
    }

    /**
     *  批量插入
     */
    public static void batchInsert(){
        List<User> lists = new ArrayList<>();
        for (int i=0; i<20;i++){
            User user = new User();
            user.setAge(i);
            user.setName("刘权 "+i+"  儿子");
            user.setAddress("川蜀");
            lists.add(user);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < lists.size(); i++) {
            User user = lists.get(i);
            session.save(user);

            // 每5条插入一次
            if (i%5==0){
                session.flush();
                session.clear();
            }
        }

        transaction.commit();
        session.close();
    }

    /**
     *  更新操作
     */
    public static void updateRecord(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from User where id=:id";
        Query query = session.createQuery(hql);
        query.setParameter("id",1);
        User user = (User) query.uniqueResult();
        user.setName("张飞");
        user.setAddress("蜀地");
        session.saveOrUpdate(user);

        transaction.commit();
        session.close();
    }

    /**
     *  根据 id删除
     */
    public static void deleteById(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "delete from User where id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id",2);
        query.executeUpdate();

        transaction.commit();
        session.close();
    }

    /**
     *  批量删除  这里可以使用 StringBuilder  对hql字符串进行拼接
     */
    public static void batchDelete(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "delete from User where id in (21,22,23)";
        Query query = session.createQuery(hql);
        query.executeUpdate();

        transaction.commit();
        session.close();
    }

    public static void main(String[] args) {
//        getAll();
//        queryWithcriteria();
//        queryWithNameCriteria();
//        queryWithLimit();
//        insert();
//        batchInsert();
//        updateRecord();
//        deleteById();
        batchDelete();
    }
}
