package pers.lcy.toutiao.model;

import java.util.Date;

public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private Date createdDate;

    private Integer hasRead;

    private String conversationId;

    private String content;


    public Message(Integer id, Integer fromId, Integer toId, Date createdDate, Integer hasRead, String conversationId) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.createdDate = createdDate;
        this.hasRead = hasRead;
        this.conversationId = conversationId;
    }

    public Message(Integer id, Integer fromId, Integer toId, Date createdDate, Integer hasRead, String conversationId, String content) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.createdDate = createdDate;
        this.hasRead = hasRead;
        this.conversationId = conversationId;
        this.content = content;
    }

    public Message() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId == null ? null : conversationId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}