package com.wk.forward;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author: Sparks
 * @Date: 2022/1/27 9:20
 * @Description
 */
public class QQForward {

    private Properties prop = new Properties();

    private Session session;

    private String MailFrom = "544094478@qq.com";

    public QQForward() throws GeneralSecurityException {
        Authenticator authenticator =  new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailFrom, "edsxumdajjbkbdhh");
            }
        };
        // transport
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.setProperty("mail.smtp.user","544094478@qq.com");
        prop.setProperty("mail.smtp.host","smtp.qq.com");
        prop.setProperty("mail.smtp.port","465");
        prop.setProperty("mail.smtp.auth","true");
        prop.setProperty("mail.smtp.ssl.enable","true");
        prop.put("mail.smtp.ssl.socketFactory",sf);
        prop.setProperty("mail.transport.protocol","smtp");

        // for store
        prop.setProperty("mail.debug","true");
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", "smtp.qq.com");

        session = Session.getDefaultInstance(prop, authenticator);
        session.setDebug(true);
    }

    public Session getSession() {
        return session;
    }

    public void forwardFirstMessage() throws MessagingException {
        // 1. get store object
        Store store = session.getStore();
        // 2. connect server
        store.connect("544094478@qq.com","edsxumdajjbkbdhh");

        // 3. 获得邮箱内的邮件夹
        Folder defaultFolder = store.getFolder("inbox");
        if (!defaultFolder.isOpen()){
            defaultFolder.open(Folder.READ_ONLY);
        }
        // 4. 获得邮件夹中的对象
        Message[] messages = defaultFolder.getMessages();

        if (messages.length < 0){
            System.out.println("no message in inbox");
            return;
        }
        // 5. 打印关键信息
        Message msg = messages[0];

        String from  = InternetAddress.toString(msg.getFrom());
        String replyTo = InternetAddress.toString(msg.getReplyTo());
        String to = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
        System.out.println("from : " + from);
        System.out.println("replyTo : " + replyTo);
        System.out.println("to : " + to);

        //6. 设置转发邮件信息头
        Message forward = new MimeMessage(session);
        // 注意这里的 from必须和 校验填写的 from一致.
        forward.setFrom(new InternetAddress(MailFrom));
        forward.setRecipient(Message.RecipientType.TO,new InternetAddress("544094478@qq.com"));
        forward.setSubject("Fwd:"+ msg.getSubject());

        //7. 设置转发邮件内容
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(msg,"message/rfc822");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        forward.setContent(multipart);
        forward.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect("544094478@qq.com","edsxumdajjbkbdhh");
        transport.sendMessage(forward, forward.getAllRecipients());


        //  close
        defaultFolder.close();
        store.close();
        transport.close();
        System.out.println("message forward successfully..");
    }

    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        QQForward qqForward = new QQForward();
        qqForward.forwardFirstMessage();
    }

}
