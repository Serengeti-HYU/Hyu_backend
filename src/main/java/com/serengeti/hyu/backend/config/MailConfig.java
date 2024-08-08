package com.serengeti.hyu.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
@Configuration
public class MailConfig {
    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Bean
    public JavaMailSender mailSender() { //JAVA MAILSENDER 구현한 객체


        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host); // 속성
        mailSender.setPort(port); // 587 포트 지정
        mailSender.setUsername(username);// 메일 전송 계정 (HYU)
        mailSender.setPassword(password); //구글 앱 비밀번호를 넣습니다.

        Properties javaMailProperties = new Properties(); // JavaMail의 속성 설정
        javaMailProperties.put("mail.transport.protocol", "smtp");// smtp 프로토콜
        javaMailProperties.put("mail.smtp.auth", "true");//smtp 서버 인증 O
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL 소켓 팩토리 클래스 사용
        javaMailProperties.put("mail.smtp.starttls.enable", "true"); // 암호화된 통신 활성화
        javaMailProperties.put("mail.debug", "true");// 디버깅 정보 출력
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.naver.com");// smtp 서버의 ssl 인증서 신뢰
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");// 사용할 ssl 프로토콜 버젼 명시

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;// 빈으로 등록한다.
    }
}