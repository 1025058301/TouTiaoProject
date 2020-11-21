package pers.lcy.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import pers.lcy.toutiao.model.News;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JedisAdapter implements InitializingBean {
    public static Logger logger= LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool=null;
    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("localhost",6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key,String value){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long scard(String key){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public void lpush(String key,String value){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public String rpop(String key){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.rpop(key);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public Long hset(String key,String filed,String value){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.hset(key,filed,value);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public String hget(String key,String filed){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.hget(key,filed);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public Map<String,String> hgetAll(String key){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.hgetAll(key);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }


    public long zremrangeByRank(String key,long start,long stop){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.zremrangeByRank(key,start,stop);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long stop){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.zrangeWithScores(key,start,stop);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public Set<String> zrevrange(String key, long start, long stop){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.zrevrange(key,start,stop);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public Long zadd(String key,double score,String member){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.zadd(key,score,member);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zadd(String key, long start, long stop){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            return jedis.zrange(key,start,stop);
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public void pushObject(String key,Object obj){
        String value= JSON.toJSONString(obj);
        Jedis jedis=null;
        try{
            jedis=getJedis();
            jedis.lpush(key,value);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
    public <T> T popObject(String key,Class<T> clazz){
        Jedis jedis=null;
        String object=null;
        try{
            jedis=getJedis();
            object=jedis.rpop(key);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return JSON.parseObject(object,clazz);
    }

    public void updateNewsLikeCount(int newsId,long likeCount){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            jedis.hset(RedisKeyUtil.getBizNewskey(newsId),RedisKeyUtil.FIELD_LIKECOUNT,String.valueOf(likeCount));
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void updateNewsCommentCount(int newsId,int commentCount){
        Jedis jedis=null;
        try {
            jedis=getJedis();
            jedis.hset(RedisKeyUtil.getBizNewskey(newsId),RedisKeyUtil.FIELD_COMMENTCOUNT,String.valueOf(commentCount));
        }catch (Exception e){
            logger.error("异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public <T> void addObejectInHashes(T t){
        Jedis jedis=null;
        Class clazz=t.getClass();
        Field[] fields=clazz.getDeclaredFields();
        String key=null;
        Map<String,String> map=new HashMap<>(16);
        try {
            jedis=getJedis();
            if (t.getClass().isAssignableFrom(News.class)) {
                Field id=clazz.getDeclaredField("id");
                id.setAccessible(true);
                key = RedisKeyUtil.getBizNewskey((int)id.get(t));
            }
            for(Field field:fields){
                field.setAccessible(true);
                map.put(field.getName(),field.get(t).toString());
            }
            jedis.hmset(key,map);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
