package pers.lcy.toutiao.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import pers.lcy.toutiao.controller.LoginController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

@Component
public class JedisAdapter implements InitializingBean {
    public static Logger logger= LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis=null;

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
}
