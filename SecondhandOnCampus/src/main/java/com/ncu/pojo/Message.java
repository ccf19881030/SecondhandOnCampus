package com.ncu.pojo;

public class Message {
    private Integer id;

    private String content;

    private Integer parentMessage;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Integer parentMessage) {
        this.parentMessage = parentMessage;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}