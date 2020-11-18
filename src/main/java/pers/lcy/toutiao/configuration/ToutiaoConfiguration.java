package pers.lcy.toutiao.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pers.lcy.toutiao.interceptor.LoginRequiredInterceptor;
import pers.lcy.toutiao.interceptor.PassportInterceptor;

import java.util.Arrays;

@Component
public class ToutiaoConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).excludePathPatterns("/error");
        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/","/user/**","/news/**","/error","/login/**","/like","/dislike","/reg/**");
        super.addInterceptors(registry);
    }
}