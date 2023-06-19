package com.tmsjsb.redpanda.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("sandbox.smtp.mailtrap.io");
    mailSender.setPort(2525);
    mailSender.setUsername("af522ebaf1337c");
    mailSender.setPassword("b76c93f1b381af");

    return mailSender;
  }

}
