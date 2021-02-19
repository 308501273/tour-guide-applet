package com.guide.common.utils;

import com.guide.conf.rabbitmq.MailBoxProperties;
import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author fengenchun
 */

@Component
public class MailBoxUtils {

    public void sendCode(String email, String code, MailBoxProperties properties){
        //做链接前的准备工作  也就是参数初始化
        Properties mailboxProperties = new Properties();
        mailboxProperties.setProperty("mail.smtp.host","smtp.qq.com");//发送邮箱服务器
        mailboxProperties.setProperty("mail.smtp.port","465");//发送端口
        mailboxProperties.setProperty("mail.smtp.auth","true");//是否开启权限控制
        mailboxProperties.setProperty("mail.transport","smtp");//发送的协议是简单的邮件传输协议
        mailboxProperties.setProperty("mail.smtp.ssl.enable","true");
        //建立两点之间的链接
        Session session = Session.getInstance(mailboxProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getEmailSender(),properties.getAuthCode());
            }
        });
        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            
            message.setFrom(new InternetAddress(properties.getEmailSender()));

            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));//收件人
            //设置主题
            message.setSubject("DoubleQ验证码");
            //设置邮件正文  第二个参数是邮件发送的类型
            String msg="您的验证码为："+code+"，有效时间："+properties.getEffectiveTime()+"分钟。您正在绑定DoubleQ的邮箱，若非本人操作，请勿泄露验证码！";
            message.setContent(msg,"text/html;charset=UTF-8");
            //发送一封邮件
            Transport transport = session.getTransport();
            transport.connect(properties.getEmailSender(),properties.getAuthCode());
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }finally {

        }

    }

}
