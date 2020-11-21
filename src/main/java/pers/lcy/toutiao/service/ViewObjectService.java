package pers.lcy.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.controller.IndexController;
import pers.lcy.toutiao.model.*;
import pers.lcy.toutiao.util.HostHolder;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ViewObjectService {
    public Logger logger= LoggerFactory.getLogger(ViewObjectService.class);
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    JedisAdapter jedisAdapter;


    public List<ViewObject> getNewsViewFromUserId(int userId) throws Exception{
        List<News> list = null;
        if (userId != 0) {
            long time=System.currentTimeMillis();
            list = newsService.getNewsByUser(userId, 0, 10);
            logger.info("数据库查数据花费"+(System.currentTimeMillis()-time)+"毫秒");
        } else {
            Set<String> highNews=newsService.getNewsIdByScore(10);
            long time=System.currentTimeMillis();
            list=new ArrayList<>();
            for(String s: highNews){
                Map<String,String> valMap=jedisAdapter.hgetAll(RedisKeyUtil.getBizNewskey(Integer.valueOf(s)));
                list.add(newsService.ConstructNewsByMap(valMap));
            }
            logger.info("redis取数据共花费"+(System.currentTimeMillis()-time)+"毫秒");
        }
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News news : list) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", news);
            viewObject.set("user", userService.getUser(news.getUserId()));
            if (hostHolder.get() != null) {
                viewObject.set("like",likeService.getLikeStatus(hostHolder.get().getId(), EntityType.NEWSTYPE,news.getId()));
            }else {
                viewObject.set("like",0);
            }
            vos.add(viewObject);
        }
        return vos;
    }

    public List<ViewObject> getCommentView(int entityId,int entityType){
        List<ViewObject> comments=new ArrayList<>();
        List<Comment> list=commentService.getCommentByEntity(entityId,entityType);
        for(Comment comment:list){
            ViewObject viewObject=new ViewObject();
            viewObject.set("comment",comment);
            viewObject.set("user",userService.getUser(comment.getUserId()));
            comments.add(viewObject);
        }
        return comments;
    }

    public List<ViewObject> getConversationDetailView(String conversationId){
        List<ViewObject> messages=new ArrayList<>();
        List<Message> details=messageService.getConversationDetail(conversationId);
        for(Message message:details){
            ViewObject viewObject=new ViewObject();
            viewObject.set("message",message);
            viewObject.set("user",userService.getUser(message.getFromId()));
            messages.add(viewObject);
        }
        return messages;
    }

    public List<ViewObject> getConversationListView(int userId){
        List<ViewObject> resConversations=new ArrayList<>();
        int localUserId=hostHolder.get().getId();
        List<Message> conversations=messageService.getConversationList(userId,0,10);
        for(Message conversation:conversations){
            ViewObject viewObject = new ViewObject();
            viewObject.set("conversation",conversation);
            int targetId=conversation.getFromId()==localUserId?conversation.getToId():conversation.getFromId();
            viewObject.set("targetUser",userService.getUser(targetId));
            viewObject.set("totalCount",conversation.getId());
            viewObject.set("unReadCount",messageService.getUnReadConversationCount(localUserId,conversation.getConversationId()));
            resConversations.add(viewObject);
        }
        return resConversations;
    }
}
