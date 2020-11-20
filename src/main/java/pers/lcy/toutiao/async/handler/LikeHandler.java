package pers.lcy.toutiao.async.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pers.lcy.toutiao.async.EventConsumer;
import pers.lcy.toutiao.async.EventModel;
import pers.lcy.toutiao.async.EventType;
import pers.lcy.toutiao.model.Message;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.model.User;
import pers.lcy.toutiao.service.MessageService;
import pers.lcy.toutiao.service.NewsService;
import pers.lcy.toutiao.service.UserService;
import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandler implements IHandler {
    public static Logger logger= LoggerFactory.getLogger(LikeHandler.class);

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    MessageService messageService;

    @Override
    public List<EventType> getAcceptEventType() {
        return Arrays.asList(EventType.LIKE);
    }

    @Override
    public void doHandle(EventModel eventModel){
        if(eventModel.getActorId()==eventModel.getEntityOwnerId()){
            return;
        }
        Message message=new Message();
        User user=userService.getUser(eventModel.getActorId());
        News news=newsService.selectNewsById(eventModel.getEntityId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        message.setContent("用户"+user.getName()+"点赞了您发布的咨询-"+news.getTitle());
        message.setFromId(eventModel.getActorId());
        message.setConversationId(eventModel.getActorId() > eventModel.getEntityOwnerId() ? eventModel.getEntityOwnerId() + "-" + eventModel.getActorId() : eventModel.getActorId() + "-" + eventModel.getEntityOwnerId());
        messageService.addMessage(message);
    }
}
