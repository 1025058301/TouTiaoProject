package pers.lcy.toutiao.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import pers.lcy.toutiao.dao.LoginTicketMapper;
import pers.lcy.toutiao.dao.UserMapper;
import pers.lcy.toutiao.model.LoginTicket;
import pers.lcy.toutiao.model.User;
import pers.lcy.toutiao.util.HostHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    public static Logger logger= LoggerFactory.getLogger(PassportInterceptor.class);
    public static final String TICKET="ticket";

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info(httpServletRequest.getRequestURI());
        if(httpServletRequest.getCookies()==null||o instanceof ResourceHttpRequestHandler){
            return true;
        }
        String ticket=null;
        for(Cookie cookie:httpServletRequest.getCookies()){
            if(cookie.getName().equals(TICKET)){
                ticket=cookie.getValue();
                break;
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getStatus()!=0||loginTicket.getExpired().before(new Date())){
                return true;
            }
            User user=userMapper.selectUserById(loginTicket.getUserId());
            hostHolder.set(user);
            logger.info(httpServletRequest.getRequestURI());
            logger.info("成功通过cookie自动登录，用户名为"+user.getName());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            if(modelAndView!=null&&hostHolder.get()!=null){
                modelAndView.addObject("user",hostHolder.get());
            }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            hostHolder.clear();
    }
}
