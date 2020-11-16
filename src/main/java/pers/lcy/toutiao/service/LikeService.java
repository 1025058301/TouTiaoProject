package pers.lcy.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getBizLikekey(entityId,entityType);
        String dislikeKey=RedisKeyUtil.getBizDislikekey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        if(jedisAdapter.sismember(dislikeKey,String.valueOf(userId))){
            return -1;
        }
        return 0;
    }

    public long addLikeUser(int userId,int entityType,int entityId){
        //增加进喜欢集合
        String likeKey= RedisKeyUtil.getBizLikekey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        //从不喜欢集合中减去
        String dislikeKey=RedisKeyUtil.getBizDislikekey(entityId,entityType);
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long addDislikeUser(int userId,int entityType,int entityId){
        String dislikeKey=RedisKeyUtil.getBizDislikekey(entityId,entityType);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        String likeKey= RedisKeyUtil.getBizLikekey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
