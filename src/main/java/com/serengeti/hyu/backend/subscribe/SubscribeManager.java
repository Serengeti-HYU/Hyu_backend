package com.serengeti.hyu.backend.subscribe;

import com.serengeti.hyu.backend.news.entity.News;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SubscribeManager {
    @Value("${spring.mail.username}")
    private String sendFrom;

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String sendTo, News newsDto) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(sendFrom);
        mimeMessage.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendTo));
        mimeMessage.setSubject(newsDto.getTitle());
        mimeMessage.setText(newsDto.getContent() + "\n" + newsDto.getLink()); // 뉴스레터 내용
        javaMailSender.send(mimeMessage);
    }

}


