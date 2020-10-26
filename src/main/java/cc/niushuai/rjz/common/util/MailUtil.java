package cc.niushuai.rjz.common.util;

import cn.hutool.core.util.CharsetUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author ns
 * @date 2020/10/26
 */
public class MailUtil {

    private static JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) ApplicationContextUtils.getBean(JavaMailSender.class);


    /**
     * 发送简单邮件
     *
     * @param title     消息标题
     * @param content   消息体
     * @param emailList 发送用户邮箱集合
     * @return void
     *
     * @author ns
     * @date 2020/2/12 15:57
     **/
    public static void sendSimpleMessage(String title, String content, List<String> emailList) throws Exception {

        sendSimpleMessage(title, content, CommonUtil.list2StringArray(emailList));
    }

    /**
     * 发送简单邮件
     *
     * @param title   主题
     * @param content 发送内容
     * @param emails  目标邮件地址
     */
    public static void sendSimpleMessage(String title, String content, String... emails) throws Exception {
        if (null == emails || emails.length == 0) {
            throw new RuntimeException("待发送邮件列表不能为空");
        }
        // 默认成功
        int result = 1;
        // 记录错误日志信息
        String remark = "";
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            JavaMailSenderImpl sender = (JavaMailSenderImpl) javaMailSender;
            simpleMailMessage.setFrom(sender.getUsername());
            simpleMailMessage.setTo(emails);
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(content);

            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            result = 0;
            remark = e.getMessage();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // mailService.mailInfoLog(tenantNo, title, content, emails, result, remark);
        }
    }


    /**
     * 稍微复杂点的邮件 不带附件
     *
     * @param title
     * @param content
     * @param emailList
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:15
     **/
    public static void sendMimeMessage(String title, String content, List<String> emailList) throws Exception {

        sendMimeMessage(title, content, CommonUtil.list2StringArray(emailList));
    }

    /**
     * 稍微复杂点的邮件 不带附件
     *
     * @param title
     * @param content
     * @param emails
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:15
     **/
    public static void sendMimeMessage(String title, String content, String... emails) throws Exception {
        // 默认成功
        int result = 1;
        // 记录错误日志信息
        String remark = "";
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            JavaMailSenderImpl sender = (JavaMailSenderImpl) javaMailSender;
            helper.setFrom(sender.getUsername());
            helper.setTo(emails);
            helper.setSubject(title);
            helper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            result = 0;
            remark = e.getMessage();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            //mailService.mailInfoLog(tenantNo, title, content, emails, result, remark);
        }
    }

    /**
     * 带附件的发送邮件
     *
     * @param title
     * @param content
     * @param attachFile
     * @param emailList
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:17
     **/
    public static void sendMimeMessageWithAttachFile(String title, String content, File attachFile, List<String> emailList) {

        sendMimeMessageWithAttachFile(title, content, attachFile, CommonUtil.list2StringArray(emailList));
    }

    /**
     * 带附件的发送邮件
     *
     * @param title
     * @param content
     * @param fileName
     * @param attachFileStream
     * @param emailList
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:17
     **/
    public static void sendMimeMessageWithAttachFile(String title, String content, String fileName, InputStream attachFileStream, List<String> emailList) {

        sendMimeMessageWithAttachFile(title, content, fileName, attachFileStream, CommonUtil.list2StringArray(emailList));
    }

    /**
     * 带附件的发送邮件
     *
     * @param title
     * @param content
     * @param fileName
     * @param attachFileStream
     * @param emails
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:17
     **/
    public static void sendMimeMessageWithAttachFile(String title, String content, String fileName, InputStream attachFileStream, String... emails) {


        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharsetUtil.UTF_8);
            helper.setFrom(javaMailSender.getUsername());
            helper.setTo(emails);
            helper.setSubject(title);
            helper.setText(content, true);

            helper.addAttachment(fileName, new InputStreamResource(attachFileStream));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带附件的发送邮件
     *
     * @param title
     * @param content
     * @param attachFile
     * @param emails
     * @return void
     *
     * @author ns
     * @date 2020/10/26 16:17
     **/
    public static void sendMimeMessageWithAttachFile(String title, String content, File attachFile, String... emails) {


        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharsetUtil.UTF_8);
            helper.setFrom(javaMailSender.getUsername());
            helper.setTo(emails);
            helper.setSubject(title);
            helper.setText(content, true);

            helper.addAttachment(attachFile.getName(), attachFile);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
