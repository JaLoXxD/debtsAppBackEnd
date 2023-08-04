package com.debts.debtsappbackend.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;
    private final String protocol;
    private final String auth;
    private final String starttls;
    private final String debug;

    public MailSenderConfig(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") Integer port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password,
            @Value("${spring.mail.properties.mail.transport.protocol}") String protocol,
            @Value("${spring.mail.properties.mail.smtp.auth}") String auth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String starttls,
            @Value("${spring.mail.properties.mail.debug.enable}") String debug
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.auth = auth;
        this.starttls = starttls;
        this.debug = debug;
    }

    @Bean("javaMailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);

        return sender;
    }
}
