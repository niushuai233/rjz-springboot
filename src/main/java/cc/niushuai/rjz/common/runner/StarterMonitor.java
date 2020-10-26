package cc.niushuai.rjz.common.runner;

import cc.niushuai.rjz.common.util.ApplicationContextUtils;
import cc.niushuai.rjz.common.util.MailUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * @author ns
 * @date 2020/10/26
 */
@Slf4j
@Component
public class StarterMonitor implements ApplicationRunner {

    @Value("${spring.mail.username}")
    private String username;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {

        String[] activeProfiles = ApplicationContextUtils.getBean(Environment.class).getActiveProfiles();
        Arrays.stream(activeProfiles).forEach(System.out::println);

        // 非 product 不发送
        if (Arrays.stream(activeProfiles).anyMatch(item -> !"prod".equalsIgnoreCase(item))) {
            log.info("非prod 不发送启动监控邮件");
            return;
        }

        MailUtil.sendMimeMessageWithAttachFile("rjz system start log", "rjz system start at " + DateUtil.now(),
                FileUtil.appendString("rjz system start at " + DateUtil.now(), FileUtil.file(FileUtil.getTmpDirPath(), "rjz-system-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".log"), CharsetUtil.UTF_8),
                username);
        log.info("发送启动监控邮件完成");
    }
}

