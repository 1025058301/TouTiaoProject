package pers.lcy.toutiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import pers.lcy.toutiao.controller.LoginController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
}
