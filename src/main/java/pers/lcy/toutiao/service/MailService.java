package pers.lcy.toutiao.service;

import org.apache.velocity.app.VelocityEngine;
import org.aspectj.lang.annotation.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private JavaMailSenderImpl javaMailSender=null;
    private String Username="1025058301@qq.com";
    private String Password="123457692fsmlw";
    private String Host="smtp.qq.com";
    private String Encoding="utf8";
    private String Protocol="smtps";
    private int Port=465;

    @Autowired
    VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("lcy");
            InternetAddress from = new InternetAddress(nick + "<1025058301@qq.com>");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setUsername(Username);
        javaMailSender.setPassword(Password);
        javaMailSender.setHost(Host);
        javaMailSender.setPort(Port);
        javaMailSender.setProtocol(Protocol);
        javaMailSender.setDefaultEncoding(Encoding);
        Properties properties=new Properties();
        properties.put("mail.smtp.ssl.enable", true);
        javaMailSender.setJavaMailProperties(properties);
    }
}
