package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "email")
public class EmailQueueListener {

    @Resource
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String username;
    @RabbitHandler
    public void sendMailMessage(Map<String,Object> map){
        String type = (String) map.get("type");
        String email = (String) map.get("email");
        Integer code = (Integer) map.get("code");
        SimpleMailMessage message =
                switch (type){
                    case "register"-> createMessage(email,"欢迎注册我们的网站","您的验证码为："+code+",您正在注册我们的网站，为了您的安全，请勿泄露验证码。");
                    case "reset" -> createMessage(email,"您正在进行密码重置","您的验证码为"+code+",您正在进行密码重置操作，若非本人操作，请无视。");
                    default -> null;
                };
        if(message == null) return;
        mailSender.send(message);
    }
    private SimpleMailMessage createMessage(String email,String title,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(content);
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(title);
        return message;
    }

}
