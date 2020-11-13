package pers.lcy.toutiao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.lcy.toutiao.model.Message;

import java.util.List;
@Mapper
public interface MessageMapper {
    void insertMessaget(Message message);

    List<Message> selectConversationDetails(@Param("conversationId") String conversationId);

    List<Message> selectConversation(@Param("userId") int userId,@Param("limit") int limit,@Param("offset") int offset);

    int selectUnReadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    void updateMessageReadStatus(@Param("userId") int userId,@Param("conversationId") String conversationId);
}