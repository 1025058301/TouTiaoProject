package pers.lcy.toutiao.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于定义不同的事件
 */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;
    private Map<String,String> environment=new HashMap<>();

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public EventModel(EventType type){
        this.type=type;
    }

    public EventModel(){

    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel setProperty(String name,String value){
        this.environment.put(name,value);
        return this;
    }

    public String getProperty(String name){
        return this.environment.get(name);
    }

}
