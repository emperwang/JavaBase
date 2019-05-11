package com.wk.SqlSession;

import com.wk.Bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyExcutor implements Excutor {
    private MyConfiguration xmlConfiguration = new MyConfiguration();

    @Override
    public <T> T query(String statement, Object parameter) {
        Connection connection = getConnection();
        ResultSet result = null;
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(statement);
            //ps = connection.prepareStatement("select * from user where id = ?");
            //设置参数
            ps.setInt(1,Integer.parseInt(parameter.toString()));
            result = ps.executeQuery();
            User user = new User();
            //遍历结果集
            while (result.next()){
                user.setId(result.getString("id"));
                user.setUserName(result.getString("username"));
                user.setPassword(result.getString("password"));
            }
            return (T)user;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private Connection getConnection(){
        try{
            Connection connection = xmlConfiguration.build("MyConfig.xml");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
