package pers.lcy.toutiao.async.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.lcy.toutiao.async.EventModel;
import pers.lcy.toutiao.async.EventType;
import pers.lcy.toutiao.service.MailService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegisterHandler implements IHandler {
    public static Logger logger= LoggerFactory.getLogger(RegisterHandler.class);

    @Autowired
    MailService mailService;

    @Override
    public void doHandle(EventModel model) {
        logger.info("调用注册发送邮件handler");
        String to=model.getProperty("to");
        logger.info("to的值："+to);
        String subject="注册成功";
        Map<String,Object> vmModel=new HashMap<>();
        vmModel.put("username",to);
        mailService.sendWithHTMLTemplate(to,subject,"mail/welcomeReg.vm",vmModel);
    }

    @Override
    public List<EventType> getAcceptEventType() {
        return Arrays.asList(EventType.REGISTER);
    }
}
