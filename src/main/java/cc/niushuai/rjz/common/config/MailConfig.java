package cc.niushuai.rjz.common.config;

import cc.niushuai.rjz.common.util.ApplicationContextUtils;
import cc.niushuai.rjz.common.util.MailUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author ns
 * @date 2020/10/26
 */
@Slf4j
@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private String port;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        this.applyProperties(javaMailSender);
        return javaMailSender;
    }

    /**
     * 设置属性值
     *
     * @param mailSender
     * @return void
     *
     * @author ns
     * @date 2020/10/26 14:35
     **/
    private void applyProperties(JavaMailSenderImpl mailSender) {

        if (StrUtil.isEmpty(host)) {
            log.error("邮件服务地址配置为空, 请检查");
            throw new RuntimeException("邮件服务地址配置为空, 请检查");
        }

        if (StrUtil.isEmpty(protocol)) {
            log.error("邮件协议配置为空, 请检查");
            throw new RuntimeException("邮件协议配置为空, 请检查");
        }

        if (StrUtil.isEmpty(port)) {
            log.error("邮件端口号配置为空, 请检查");
            throw new RuntimeException("邮件端口号配置为空, 请检查");
        }

        if (StrUtil.isEmpty(username)) {
            log.error("邮件发件人地址配置为空, 请检查");
            throw new RuntimeException("邮件发件人地址配置为空, 请检查");
        }

        if (StrUtil.isEmpty(password)) {
            log.error("邮件发件人密码配置为空, 请检查");
            throw new RuntimeException("邮件发件人密码配置为空, 请检查");
        }

        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);
        mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        mailSender.setJavaMailProperties(this.getProperties());

        try {
            mailSender.testConnection();

        } catch (MessagingException e) {
            log.error("测试邮件配置出错: {}", e.getMessage(), e);
        }

        log.info("Mail Config Host: {}", host);
        log.info("Mail Config protocol: {}", protocol);
        log.info("Mail Config port: {}", port);
        log.info("Mail Config username: {}", username);
        log.info("Mail Config password: {}", password);
    }

    private Properties getProperties() {

        Properties props = new Properties();

        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.starttls.enable", "true");
        props.put("mail.smtp.ssl.starttls.required", "true");

        return props;
    }
}
