package pers.lcy.toutiao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    public static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* pers.lcy.toutiao.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuffer s=new StringBuffer();
        for(Object o:joinPoint.getArgs()){
            s.append(o.toString());
        }
        logger.info("before the method  "+s);
    }

    @After("execution(* pers.lcy.toutiao.controller.IndexController.*(..))")
    public void afterMehtod(JoinPoint joinPoint){
        logger.info("after the method");
    }

}
