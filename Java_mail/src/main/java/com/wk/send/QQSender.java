package com.wk.send;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author: Sparks
 * @Date: 2022/1/26 16:01
 * @Description
 */
public class QQSender {
    private Properties prop = new Properties();

    private Session session;

    public QQSender() throws GeneralSecurityException {
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.setProperty("mail.smtp.user","544094478@qq.com");
        prop.setProperty("mail.smtp.host","smtp.qq.com");
        prop.setProperty("mail.smtp.port","465");
        prop.setProperty("mail.smtp.auth","true");
        prop.setProperty("mail.smtp.ssl.enable","true");
        prop.put("mail.smtp.ssl.socketFactory",sf);
        prop.setProperty("mail.transport.protocol","smtp");

        session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("544094478@qq.com", "edsxumdajjbkbdhh");
            }
        });
        session.setDebug(true);
    }

    public Session getSession() {
        return session;
    }

    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        String user = "544094478@qq.com";
        QQSender QQSender = new QQSender();
        Session session = QQSender.getSession();
        Transport transport = session.getTransport();
        //连接服务器
        transport.connect();
        //transport.connect("smtp.qq.com","544094478@qq.com","edsxumdajjbkbdhh");

        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 邮件发送人
        message.setFrom(user);
        // 邮件接收人
        message.setRecipients(Message.RecipientType.TO, user);
         // 邮件标题
        message.setSubject("Hello Mail");
        // 邮件内容
        message.setContent("first mail from javeMail","text/html;charset=UTF-8");

        // 发送邮件
        transport.sendMessage(message,message.getAllRecipients());

        // 关闭连接
        transport.close();
    }

}
