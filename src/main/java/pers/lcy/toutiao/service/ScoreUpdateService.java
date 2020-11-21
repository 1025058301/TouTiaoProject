package pers.lcy.toutiao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.model.News;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

@Service
@EnableAsync
public class ScoreUpdateService {
    public static Logger logger= LoggerFactory.getLogger(ScoreUpdateService.class);

    @Autowired
    NewsService newsService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Scheduled(fixedDelay = 1000*60*60)
    @Async
    public void  updateScore(){
        logger.info("开始每隔一小时进行一次的资讯分数更新操作");
        long beforeTime=System.currentTimeMillis();
        List<News> latestNews=newsService.getLatestNews(100);
        Set<Tuple> allVal=jedisAdapter.zrangeWithScores(RedisKeyUtil.getBIZNewsScoreKey(),0,-1);
        for(Tuple tuple:allVal){
            jedisAdapter.zadd(RedisKeyUtil.getBIZNewsScoreKey(),tuple.getScore()*0.1,tuple.getElement());
        }
        for(News news:latestNews){
            long exsitTime=System.currentTimeMillis()-news.getCreatedDate().getTime();
            int likeCount=news.getLikeCount();
            double score=(likeCount+1)/Math.pow((double) (2+exsitTime/1000/60/60),1.5);
            jedisAdapter.zadd(RedisKeyUtil.getBIZNewsScoreKey(),score,String.valueOf(news.getId()));
        }
        logger.info("更新完成，共耗费时间"+(System.currentTimeMillis()-beforeTime)+"毫秒");
    }
}
