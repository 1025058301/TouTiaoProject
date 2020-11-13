package pers.lcy.toutiao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pers.lcy.toutiao.service.NewsService;
import pers.lcy.toutiao.service.UserService;
import pers.lcy.toutiao.service.ViewObjectService;
import pers.lcy.toutiao.util.HostHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author lcy
 */
@Controller
public class IndexController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    ViewObjectService viewObjectService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos",viewObjectService.getNewsViewFromUserId(0));
        if(hostHolder.get()!=null){
            pop=0;
        }
        model.addAttribute("pop",pop);
        return "home";
    }

    @RequestMapping("/user/{userId}")
    public String userIndex(Model model,@PathVariable("userId") int userId){
        model.addAttribute("vos",viewObjectService.getNewsViewFromUserId(userId));
        return "home";
    }

    @RequestMapping("/show/{userid}/{key}")
    @ResponseBody
    public String show(@PathVariable("userid") String id,
                       @PathVariable("key") int key,
                       @RequestParam(value = "type", defaultValue = "1") String type) {
        return id + " " + key + " " + type;
    }

    @RequestMapping("/vm")
    public String news(Model model) {
        model.addAttribute("value1", "123");
        List<String> list = Arrays.asList(new String[]{"blue", "red", "white"});
        model.addAttribute("colorlist", list);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(16);
        for (int i = 0; i < 4; i++) {
            map.put(i, i * i);
        }
        model.addAttribute("map", map);
        return "news";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
       Enumeration<String> enumeration= request.getHeaderNames();
       StringBuffer stringBuffer=new StringBuffer();
       while (enumeration.hasMoreElements()){
            String s=enumeration.nextElement();
            stringBuffer.append(s+" "+request.getHeader(s)+"<br>");
       }
       for(Cookie cookie:request.getCookies()){
           stringBuffer.append(cookie.getName());
           stringBuffer.append(":");
           stringBuffer.append(cookie.getValue()+"<br>");
       }
       return new String(stringBuffer);
    }

    @RequestMapping("/resbonse")
    @ResponseBody
    public String resbonse(@CookieValue(value = "classid",defaultValue = "1") String classid,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addHeader(key,value);
        response.addCookie(new Cookie(key,value));
        return "classid :"+classid;
    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirectView(@PathVariable("code") int code,
                                     HttpSession session){
            RedirectView redirectView=new RedirectView("/",true);
            if(code==301){
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            }
            session.setAttribute("msg","页面被重定向啦");
            return redirectView;
            //return "redirece:/" 302跳转
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "admin",required = false)String admin){
        if(admin.equals("lcy")){return "Hello lcy";}
        throw new IllegalArgumentException("oh no you are not lcy");

    }

    @ExceptionHandler
    @ResponseBody
    public String handlerException(Exception e){
        return "出现异常："+e.getMessage();
    }
}
