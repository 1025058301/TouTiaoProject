
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.lcy.toutiao.ToutiaoApplication;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.dao.*;
import pers.lcy.toutiao.model.User;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class InitDatabaseTests {
    @Autowired
    NewsMapper newsMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    public void initData() {
        Random random=new Random();
        for(int i=0;i<10;i++){
            User user=new User();
            user.setName(String.format("user:%d",random.nextInt(10000)));
            user.setHeadUrl(String.format("www.nowcode.com/%d.jpg",i));
            user.setPassword("000000");
            user.setSalt("123");
            userMapper.addUser(user);
            News news=new News();
            news.setTitle(String.format("这是一个标题%d",i));
            news.setLink(String.format("www.link.com/%d",random.nextInt(100)));
            news.setImage("nophoto.jpg");
            news.setUserId(i);
            news.setLikeCount(random.nextInt(100));
            news.setCommentCount(random.nextInt(45));
            newsMapper.insertNews(news);
        }

    }

}

