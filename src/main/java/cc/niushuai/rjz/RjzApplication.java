package cc.niushuai.rjz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RjzApplication {

    public static void main(String[] args) {
        SpringApplication.run(RjzApplication.class, args);
    }
}
