package pers.lcy.toutiao.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.lcy.toutiao.async.EventModel;
import pers.lcy.toutiao.async.EventProducer;
import pers.lcy.toutiao.async.EventType;
import pers.lcy.toutiao.model.EntityType;
import pers.lcy.toutiao.service.LikeService;
import pers.lcy.toutiao.service.NewsService;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;
import pers.lcy.toutiao.util.JedisAdapter;

@Controller
public class LikeController {
    public Logger logger= LoggerFactory.getLogger(LikeController.class);
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(value = "/like",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        if(hostHolder.get()==null){
            return CommonUtil.getJsonString(1,"请登陆后再进行点赞操作");
        }
        logger.info("有用户点赞了");
        long likeCount=likeService.addLikeUser(hostHolder.get().getId(), EntityType.NEWSTYPE,newsId);
        jedisAdapter.updateNewsLikeCount(newsId,likeCount);
        newsService.updateNewsLikeCount(newsId,(int)likeCount);
        eventProducer.produceEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.get().getId())
                .setEntityId(newsId).setEntityId(newsId).setEntityId(newsId).setEntityType(EntityType.NEWSTYPE)
                .setEntityOwnerId(newsService.selectNewsById(newsId).getUserId())
        );
        return CommonUtil.getJsonString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        if(hostHolder.get()==null){
            return CommonUtil.getJsonString(1,"请登陆后再进行点踩操作");
        }
        long likeCount=likeService.addDislikeUser(hostHolder.get().getId(), EntityType.NEWSTYPE,newsId);
        newsService.updateNewsLikeCount(newsId,(int)likeCount);
        return CommonUtil.getJsonString(0,String.valueOf(likeCount));
    }
}
