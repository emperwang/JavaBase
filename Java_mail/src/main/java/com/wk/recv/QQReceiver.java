package com.wk.recv;

import javax.mail.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author: Sparks
 * @Date: 2022/1/26 16:01
 * @Description
 */
public class QQReceiver {
    private Properties prop = new Properties();

    private Session session;

    public QQReceiver() throws GeneralSecurityException {
        /*
        DEBUG POP3: mail.pop3.rsetbeforequit: false
        DEBUG POP3: mail.pop3.disabletop: false
        DEBUG POP3: mail.pop3.forgettopheaders: false
        DEBUG POP3: mail.pop3.cachewriteto: false
        DEBUG POP3: mail.pop3.filecache.enable: false
        DEBUG POP3: mail.pop3.keepmessagecontent: false
        DEBUG POP3: mail.pop3.starttls.enable: false
        DEBUG POP3: mail.pop3.starttls.required: false
        DEBUG POP3: mail.pop3.finalizecleanclose: false
        DEBUG POP3: mail.pop3.apop.enable: false
        DEBUG POP3: mail.pop3.disablecapa: false
        DEBUG POP3: connecting to host "smtp.qq.com", port 110, isSSL false
         */
        prop.setProperty("mail.debug","true");
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", "smtp.qq.com");

        Authenticator authenticator =  new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("544094478@qq.com", "edsxumdajjbkbdhh");
            }
        };

        session = Session.getDefaultInstance(prop);
        session.setDebug(true);
    }

    public Session getSession() {
        return session;
    }


    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        QQReceiver receiver = new QQReceiver();
        Session session = receiver.getSession();

        // 1. get store object
        Store store = session.getStore();
        // 2. connect server
        store.connect("544094478@qq.com","edsxumdajjbkbdhh");

        // 3. 获得邮箱内的邮件夹
        Folder defaultFolder = store.getFolder("inbox");
        if (!defaultFolder.isOpen()){
            defaultFolder.open(Folder.READ_WRITE);
        }
        // 4. 获得邮件夹中的对象
        Message[] messages = defaultFolder.getMessages();
        for(int i=0; i<messages.length; i++){
            String from = messages[i].getFrom()[0].toString();
            String subject = messages[i].getSubject();
            System.out.println("from: " + from);
            System.out.println("subject:" + subject);
        }
        // 5. close
        store.close();
    }
}
