package pers.lcy.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.async.handler.IHandler;
import pers.lcy.toutiao.util.JedisAdapter;
import pers.lcy.toutiao.util.RedisKeyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    public static Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;
    private Map<EventType, List<IHandler>> config=new HashMap<>();

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,IHandler> handlerMap=applicationContext.getBeansOfType(IHandler.class);
        if(handlerMap==null){
            logger.warn("容器中没有实现了IHandler接口的bean");
            return;
        }
        for(Map.Entry<String,IHandler> entry:handlerMap.entrySet()){
            for (EventType eventType:entry.getValue().getAcceptEventType()){
                if(!config.containsKey(eventType)){
                    config.put(eventType,new ArrayList<IHandler>());
                }
                config.get(eventType).add(entry.getValue());
            }
        }

        Executor executor= new ThreadPoolExecutor(1,1,1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String event=jedisAdapter.rpop(RedisKeyUtil.getBizEvent());
                    if(event==null)continue;
                    EventModel eventModel= JSONObject.parseObject(event,EventModel.class);
                    if(!config.containsKey(eventModel.getType())){
                        logger.error("不能识别的事件");
                        continue;
                    }
                    for(IHandler handler:config.get(eventModel.getType())){
                        handler.doHandle(eventModel);
                    }
                }
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
