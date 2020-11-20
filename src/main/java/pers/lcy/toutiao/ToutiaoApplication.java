package pers.lcy.toutiao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ToutiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToutiaoApplication.class, args);
    }

}
