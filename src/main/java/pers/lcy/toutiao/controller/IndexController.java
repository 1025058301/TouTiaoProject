package pers.lcy.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Logger logger= LoggerFactory.getLogger(IndexController.class);
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
        try {
            model.addAttribute("vos",viewObjectService.getNewsViewFromUserId(0));
            if(hostHolder.get()!=null){
                pop=0;
            }
            model.addAttribute("pop",pop);
            return "home";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }

    @RequestMapping("/user/{userId}")
    public String userIndex(Model model,@PathVariable("userId") int userId,
                            @RequestParam(value = "pop",defaultValue = "0") int pop){
        try {
            model.addAttribute("vos",viewObjectService.getNewsViewFromUserId(userId));
            if(hostHolder.get()!=null){
                pop=0;
            }
            model.addAttribute("pop",pop);
            return "home";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }




    @ExceptionHandler
    @ResponseBody
    public String handlerException(Exception e){
        e.printStackTrace();
        return "出现异常："+e.getMessage();
    }
}
