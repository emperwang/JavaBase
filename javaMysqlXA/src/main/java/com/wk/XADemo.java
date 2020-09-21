package com.wk;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class XADemo {
    private static String connstr1 = "jdbc:mysql://127.0.0.1:3306/bj";
    private static String connstr2 = "jdbc:mysql://127.0.0.1:3306/sh";
    private static String user = "root";
    private static String pass = "admin";
    public static void main(String[] args) throws SQLException, XAException {
        // insertValu();
        rollback();
    }

    public static void rollback(){
        MysqlXADataSource xaDs1 = getDataSource(connstr1, user, pass);
        MysqlXADataSource xaDs2 = getDataSource(connstr2, user, pass);
        MyXid myXid = new MyXid(100, new byte[]{0x01}, new byte[]{0x02});
        MyXid my2 = new MyXid(100, new byte[]{0x11}, new byte[]{0x22});

        XAConnection xaConnection=null;
        XAResource xaResource=null;
        Connection connection=null;
        Statement statement=null;

        XAConnection xaConnection1=null;
        XAResource xaResource1=null;
        Connection connection1=null;
        Statement statement1=null;

        try {
            xaConnection = xaDs1.getXAConnection();
            xaResource = xaConnection.getXAResource();
            connection = xaConnection.getConnection();
            statement = connection.createStatement();

            xaConnection1 = xaDs2.getXAConnection();
            xaResource1 = xaConnection1.getXAResource();
            connection1 = xaConnection1.getConnection();
            statement1 = connection1.createStatement();


            xaResource.start(myXid, XAResource.TMNOFLAGS);
            statement.execute("update account set money=money-1000 where name='david'");
            xaResource.end(myXid, XAResource.TMSUCCESS);

            int i = 1/0;

            xaResource1.start(my2, XAResource.TMNOFLAGS);
            statement1.execute("update account set money=money+1000 where name='david'");
            xaResource1.end(my2, XAResource.TMSUCCESS);

            // prepare
            int prepare12 = xaResource.prepare(myXid);
            int prepare = xaResource1.prepare(my2);

            // commit
            if (prepare12 == XAResource.XA_OK && prepare == XAResource.XA_OK) {
                xaResource.commit(myXid, false);
                xaResource1.commit(my2, false);
            }else{
                xaResource.rollback(myXid);
                xaResource1.rollback(my2);
            }
        } catch (SQLException e) {
            if (xaResource != null && xaResource1 != null) {
                try {
                    xaResource.rollback(myXid);
                    xaResource1.rollback(my2);
                } catch (XAException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (xaResource != null && xaResource1 != null) {
                try {
                    xaResource.rollback(myXid);
                    xaResource1.rollback(my2);
                } catch (XAException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void insertValu(){
        MysqlXADataSource xaDs1 = getDataSource(connstr1, user, pass);
        MysqlXADataSource xaDs2 = getDataSource(connstr2, user, pass);
        MyXid myXid = new MyXid(100, new byte[]{0x01}, new byte[]{0x02});
        MyXid my2 = new MyXid(100, new byte[]{0x11}, new byte[]{0x22});

        XAConnection xaConnection=null;
        XAResource xaResource=null;
        Connection connection=null;
        Statement statement=null;

        XAConnection xaConnection1=null;
        XAResource xaResource1=null;
        Connection connection1=null;
        Statement statement1=null;

        try {
            xaConnection = xaDs1.getXAConnection();
            xaResource = xaConnection.getXAResource();
            connection = xaConnection.getConnection();
            statement = connection.createStatement();

            xaConnection1 = xaDs2.getXAConnection();
            xaResource1 = xaConnection1.getXAResource();
            connection1 = xaConnection1.getConnection();
            statement1 = connection1.createStatement();


            xaResource.start(myXid, XAResource.TMNOFLAGS);
            statement.execute("update account set money=money-1000 where name='david'");
            xaResource.end(myXid, XAResource.TMSUCCESS);

            xaResource1.start(my2, XAResource.TMNOFLAGS);
            statement1.execute("update account set money=money+1000 where name='david'");
            xaResource1.end(my2, XAResource.TMSUCCESS);

            // prepare
            int prepare12 = xaResource.prepare(myXid);
            int prepare = xaResource1.prepare(my2);

            // commit
            if (prepare12 == XAResource.XA_OK && prepare == XAResource.XA_OK) {
                xaResource.commit(myXid, false);
                xaResource1.commit(my2, false);
            }else{
                xaResource.rollback(myXid);
                xaResource1.rollback(my2);
            }
        } catch (SQLException e) {
            if (xaResource != null && xaResource1 != null) {
                try {
                    xaResource.rollback(myXid);
                    xaResource1.rollback(my2);
                } catch (XAException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (XAException e) {
            if (xaResource != null && xaResource1 != null) {
                try {
                    xaResource.rollback(myXid);
                    xaResource1.rollback(my2);
                } catch (XAException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static MysqlXADataSource getDataSource(String connStr, String user,String password){
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl(connStr);
        xaDataSource.setUser(user);
        xaDataSource.setPassword(password);
        return xaDataSource;
    }


}
