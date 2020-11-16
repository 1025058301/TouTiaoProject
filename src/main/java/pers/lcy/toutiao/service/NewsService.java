package pers.lcy.toutiao.service;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.dao.NewsMapper;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NewsService {
    public static Logger logger= LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    HostHolder hostHolder;

    public List<News> getNews(int userId, int offset, int limit) {
        return newsMapper.selectByUserIdAndOffset(userId, offset, limit);
    }
    public News selectNewsById(int newsId){
        return newsMapper.selectById(newsId);
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
        newsMapper.insertNews(news);
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
}

