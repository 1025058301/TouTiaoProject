package pers.lcy.toutiao.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.service.NewsService;
import pers.lcy.toutiao.service.QiniuCloudService;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Date;

@Controller
public class NewsController {
    public Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuCloudService qiniuCloudService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/uploadImage", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl=qiniuCloudService.saveImage(file);
            if (fileUrl == null) {
                logger.warn("图片上传失败" + file.getOriginalFilename());
                return CommonUtil.getJsonString(1, "上传图片失败");
            }
            logger.info(file.getOriginalFilename() + "上传成功");
            return CommonUtil.getJsonString(0, fileUrl);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("图片上传失败");
            return CommonUtil.getJsonString(1, "上传失败");
        }
    }

    @RequestMapping(value = "/image", method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                           HttpServletResponse response){
        response.setContentType("image/jpg");
        try {
            StreamUtils.copy(new FileInputStream(new File(CommonUtil.ImageStorePath+imageName)),response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/user/addNews", method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news=new News();
            news.setUserId(hostHolder.get().getId());
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            news.setCommentCount(0);
            news.setLikeCount(0);
            newsService.addNews(news);
            return CommonUtil.getJsonString(0);
        }catch (Exception e){
            logger.error(e.getMessage());
            return CommonUtil.getJsonString(1,"资讯发布失败");
        }
    }
}
