package pers.lcy.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.dao.NewsMapper;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NewsService {
    public static Logger logger= LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;

    public List<News> getNewsByUser(int userId, int offset, int limit) {
        return newsMapper.selectByUserIdAndOffset(userId, offset, limit);
    }

    public List<News> getLatestNews(int num){
        return newsMapper.selectLatestNews(num);
    }

    public News selectNewsById(int newsId){
        return newsMapper.selectById(newsId);
    }

    public Set<String> getNewsIdByScore(int num){
        return jedisAdapter.zrevrange(RedisKeyUtil.getBIZNewsScoreKey(),0,num-1);
    }

    public Map<String,Object> addNews(String image, String title, String link){
        Map<String,Object> res= new HashMap<>();
        if(!title.matches(CommonUtil.NewsTitleNormalRegex)){
            res.put("msg","标题只能由中文，数字，字母组成");
            return res;
        }
        if(title.matches(CommonUtil.MinganRegex)){
            res.put("msg","请检查标题中是否存在不恰当词汇");
            return res;
        }
        if(!link.matches(CommonUtil.LinkRegex)){
            res.put("msg","链接地址不符合标准");
            return res;
        }
        News news=new News();
        news.setImage(image);
        news.setTitle(title);
        news.setLink(link);
        news.setLikeCount(0);
        news.setUserId(hostHolder.get().getId());
        news.setCommentCount(0);
        news.setDeleteState(0);
        news.setCreatedDate(new Date());
        newsMapper.insertNews(news);
        jedisAdapter.zadd(RedisKeyUtil.getBIZNewsScoreKey(),1/Math.pow(2,1.5),String.valueOf(news.getId()));
        jedisAdapter.addObejectInHashes(news);
        return null;
    }

    public String saveImage(MultipartFile file) throws Exception {
        int dotIndex = file.getOriginalFilename().lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(dotIndex+1).toLowerCase();
        logger.info(fileExt);
        if(!CommonUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new java.io.File(CommonUtil.ImageStorePath+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return CommonUtil.Domain+"image?name="+fileName;
    }

    public void updateNewsCommentCount(int newsId,int Commentcount){
        newsMapper.updateCommentCount(newsId,Commentcount);
    }

    public void updateNewsLikeCount(int newsId, int likeCount) {
        newsMapper.updateLikeCount(newsId, likeCount);
    }

    public News ConstructNewsByMap(Map<String,String> map) throws Exception{
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
        News news=new News();
        news.setId(Integer.valueOf(map.get("id")));
        news.setImage(map.get("image"));
        news.setTitle(map.get("title"));
        news.setLink(map.get("link"));
        news.setLikeCount(Integer.valueOf(map.get("likeCount")));
        news.setUserId(Integer.valueOf(map.get("userId")));
        news.setCommentCount(Integer.valueOf(map.get("commentCount")));
        news.setDeleteState(Integer.valueOf(map.get("deleteState")));
        news.setCreatedDate(new Date(map.get("createdDate")));
        return news;
    }
}

