package pers.lcy.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.async.handler.RegisterHandler;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

@Service
public class EventProducer {
    public static Logger logger= LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean produceEvent(EventModel model){
        try{
            String value= JSONObject.toJSONString(model);
            logger.info("事件保存的状态："+value);
            String key= RedisKeyUtil.getBizEvent();
            jedisAdapter.lpush(key,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
