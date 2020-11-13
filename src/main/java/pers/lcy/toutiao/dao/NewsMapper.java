package pers.lcy.toutiao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.lcy.toutiao.model.News;

import java.util.List;
@Mapper
public interface NewsMapper {

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,
    @Param("limit") int limit);

    void insertNews(News news);

    News selectById(@Param("newsId") int id);

    void updateCommentCount(@Param("newsId") int newsId,@Param("commentCount") int count);
}