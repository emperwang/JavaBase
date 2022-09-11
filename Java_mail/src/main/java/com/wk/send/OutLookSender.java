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
public class OutLookSender {
    private Properties prop = new Properties();

    private Session session;

    public OutLookSender() throws GeneralSecurityException {
        prop.setProperty("mail.smtp.user","jason.k.wang_sp@hsbc.com");
        prop.setProperty("mail.smtp.host","smtp.office365.com");
        prop.setProperty("mail.smtp.port","587");
        prop.setProperty("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.transport.protocol","smtp");

        session = Session.getDefaultInstance(prop);
        session.setDebug(true);
    }

    public Session getSession() {
        return session;
    }

    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        String user = "jason.k.wang_sp@hsbc.com";
        OutLookSender QQSender = new OutLookSender();
        Session session = QQSender.getSession();
        Transport transport = session.getTransport();
        //连接服务器
        //transport.connect();
        transport.connect(user,"qazwsx@#175868");

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
