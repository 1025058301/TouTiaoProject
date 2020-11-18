package pers.lcy.toutiao.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.lcy.toutiao.ToutiaoApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class MailServiceTest {
    @Autowired
    MailService mailService;

    @Test
    public void sendWithHTMLTemplate() {
        Map<String,Object> map=new HashMap<>();
        map.put("username","小李子");
        mailService.sendWithHTMLTemplate("1025058301@qq.com","你的作页怎么还不交","mail/welcomeReg.vm",
                map);
    }
}