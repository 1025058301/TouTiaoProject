package pers.lcy.toutiao.async.handler;

import pers.lcy.toutiao.async.EventModel;
import pers.lcy.toutiao.async.EventType;

import java.util.ArrayList;
import java.util.List;

public interface IHandler {
    void doHandle(EventModel model);
    List<EventType> getAcceptEventType();

}
