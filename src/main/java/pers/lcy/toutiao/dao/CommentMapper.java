package pers.lcy.toutiao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.lcy.toutiao.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);

    int selectCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType );

    List<Comment> selectCommentsByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

}