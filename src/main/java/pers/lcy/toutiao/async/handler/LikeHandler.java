package pers.lcy.toutiao.async.handler;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pers.lcy.toutiao.async.EventModel;
import pers.lcy.toutiao.async.EventType;

import java.util.Arrays;
import java.util.List;
@Component
public class LikeHandler implements IHandler {
    @Override
    public List<EventType> getAcceptEventType() {
        return Arrays.asList(EventType.LIKE);
    }

    @Override
    public void doHandle(EventModel eventModel){

    }
}
