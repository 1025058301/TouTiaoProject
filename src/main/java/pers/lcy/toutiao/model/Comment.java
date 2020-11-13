package pers.lcy.toutiao.model;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userId;

    private Integer entityId;

    private Integer entityType;

    private Date createdDate;

    private Integer status;

    private String content;

    public Comment(Integer id, Integer userId, Integer entityId, Integer entityType, Date createdDate, Integer status) {
        this.id = id;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.createdDate = createdDate;
        this.status = status;
    }

    public Comment(Integer id, Integer userId, Integer entityId, Integer entityType, Date createdDate, Integer status, String content) {
        this.id = id;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.createdDate = createdDate;
        this.status = status;
        this.content = content;
    }

    public Comment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}