package pers.lcy.toutiao.async;

public enum EventType {
    /**
     * LIKE点赞事件 EMAIL邮件事件 COMMENT评论事件
     */
    LIKE(0),
    EMAIL(1),
    COMMENT(2),
    REGISTER(3);
    private int value;
    private EventType(int val){
        this.value=val;
    }
    public int getValue(){
        return this.value;
    }
}
