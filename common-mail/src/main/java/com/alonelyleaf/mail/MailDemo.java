package com.alonelyleaf.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

/**
 *
 * 问题整理：
 * 向邮箱服务器投递时，如果包含无效邮箱：
 * 1.如果无效邮箱域名为阿里云的，则会被退信，但抛出的异常里会包含所有无效邮箱及邮箱邮箱
 * 2.如果域名是
 *
 * @author bijl
 * @date 2020/3/19
 */
public class MailDemo {

    private static final String MAIL_SERVER = "smtp.qq.com";
    private static final String AUTH_USERNAME = "my@qq.com";
    private static final String AUTH_PASSWORD = "password";
    private static final String MAIL_FROM = "my@qq.com";
    private static final String MAIL_TO_1 = "myMail1@qq.com";
    private static final String MAIL_TO_2 = "myMail2@qq.com";

    public static void main(String[] args) {
        try {
            //设置SSL连接、邮件环境
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", MAIL_SERVER);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            //建立邮件会话
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                //身份认证
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AUTH_USERNAME, AUTH_PASSWORD);
                }
            });
            //建立邮件对象
            MimeMessage message = new MimeMessage(session);
            //设置邮件的发件人、收件人、主题
            //附带发件人名字
//            message.setFrom(new InternetAddress("from_mail@qq.com", "optional-personal"));
            message.setFrom(new InternetAddress(MAIL_FROM));

            message.addRecipients(Message.RecipientType.TO, MAIL_TO_2);
            message.addRecipients(Message.RecipientType.TO, MAIL_TO_1);
            //message.addRecipients(Message.RecipientType.TO, MAIL_TO_2);
            message.setSubject("subject test");
            //文本
            String content="mail content test";
            message.setText(content);
            message.setSentDate(new Date());
            message.saveChanges();
            //发送邮件

            Transport transport = session.getTransport("smtp");
            try {
                transport.connect();
            } catch (MessagingException msex) {
                System.out.println(msex);
            }

            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {



            System.out.println(e);
        }
    }
}
