package pers.lcy.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.dao.MessageMapper;
import pers.lcy.toutiao.model.Message;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    public void addMessage(Message message) {
        messageMapper.insertMessaget(message);
    }

    public List<Message> getConversationDetail(String conversationId){
       return messageMapper.selectConversationDetails(conversationId);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageMapper.selectConversation(userId,limit,offset);
    }

    public int getUnReadConversationCount(int userId,String conversationId){
        return messageMapper.selectUnReadCount(userId, conversationId);
    }

    public void setMessageReadStatus(int userId,String conversationId){
        messageMapper.updateMessageReadStatus(userId,conversationId);
    }
}
