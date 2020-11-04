package pers.lcy.toutiao.service;

import org.apache.tomcat.jni.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.dao.NewsMapper;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.util.CommonUtil;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    public static Logger logger= LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsMapper newsMapper;

    public List<News> getNews(int userId, int offset, int limit) {
        return newsMapper.selectByUserIdAndOffset(userId, offset, limit);
    }

    public void addNews(News news){
        newsMapper.insertNews(news);
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
}
