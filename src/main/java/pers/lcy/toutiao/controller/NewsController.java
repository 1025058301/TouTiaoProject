package pers.lcy.toutiao.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.model.*;
import pers.lcy.toutiao.service.*;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    public Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuCloudService qiniuCloudService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    ViewObjectService viewObjectService;

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
            Map<String, Object> result = newsService.addNews(image, title, link);
            if (result != null) {
                return CommonUtil.getJsonString(1, result.get("msg").toString());
            }
            return CommonUtil.getJsonString(0);
        } catch (Exception e) {
            logger.error(e.toString());
            return CommonUtil.getJsonString(1, "资讯发布失败");
        }
    }

    @RequestMapping(value = "/news/{newsId}", method = {RequestMethod.GET})
    public String getNewsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.selectNewsById(newsId);
        User user = userService.getUser(news.getUserId());
        List<ViewObject> comments=viewObjectService.getCommentView(newsId,EntityType.NEWSTYPE);
        model.addAttribute("comments",comments);
        model.addAttribute("news", news);
        model.addAttribute("owner", user);
        return "detail";
    }

    @RequestMapping(value="/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        logger.info(newsId+" "+content);
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.NEWSTYPE);
            comment.setStatus(0);
            comment.setUserId(hostHolder.get().getId());
            commentService.addComment(comment);
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateNewsCommentCount(newsId,count);
        }catch (Exception e){
            logger.error("提交评论错误"+e.toString());
        }
        return "redirect:/news/"+newsId;
    }
}
