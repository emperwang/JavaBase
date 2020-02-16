package com.wk.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class PostgresqlUtil {
    private static Reader reader;
    public static SqlSessionFactory sessionFactory;
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

    public static SqlSessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
