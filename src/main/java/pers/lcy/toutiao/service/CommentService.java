package pers.lcy.toutiao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.dao.CommentMapper;
import pers.lcy.toutiao.model.Comment;

import java.util.List;

@Service
public class CommentService {
    public static Logger logger= LoggerFactory.getLogger(CommentService.class);

    @Autowired
    CommentMapper commentMapper;

    public void addComment(Comment comment){
        commentMapper.insertComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
       return commentMapper.selectCommentCount(entityId,entityType);
    }

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentMapper.selectCommentsByEntity(entityId,entityType);
    }

}
