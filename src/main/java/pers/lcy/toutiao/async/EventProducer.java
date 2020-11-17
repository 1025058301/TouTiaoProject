package pers.lcy.toutiao.async;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean produceEvent(EventModel model){
        try{
            String value= JSON.toJSONString(model);
            String key= RedisKeyUtil.getBizEvent();
            jedisAdapter.lpush(key,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
