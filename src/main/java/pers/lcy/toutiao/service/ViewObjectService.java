package pers.lcy.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.model.ViewObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewObjectService {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    public List<ViewObject> getViewObjectFromUserId(int userId){
        List<News> list=newsService.getNews(userId,0,10);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for (News news:list) {
            ViewObject viewObject=new ViewObject();
            viewObject.set("news",news);
            viewObject.set("user",userService.getUser(news.getUserId()));
            vos.add(viewObject);
        }
        return vos;
    }
}
