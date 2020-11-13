package pers.lcy.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.model.Comment;
import pers.lcy.toutiao.model.Message;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.model.ViewObject;
import pers.lcy.toutiao.util.HostHolder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewObjectService {
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


    public List<ViewObject> getNewsViewFromUserId(int userId){
        List<News> list=newsService.getNews(userId,0,10);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for (News news:list) {
            ViewObject viewObject=new ViewObject();
            viewObject.set("news",news);
            viewObject.set("user",userService.getUser(news.getUserId()));
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
