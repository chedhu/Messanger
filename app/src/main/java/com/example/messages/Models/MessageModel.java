package com.example.messages.Models;

public class MessageModel {

    String wUId, wMessage, wMessageId;
    Long wTimestamp;

    public MessageModel(String wUId, String wMessage, Long wTimestamp) {
        this.wUId = wUId;
        this.wMessage = wMessage;
        this.wTimestamp = wTimestamp;
    }

    public MessageModel(String wUId, String wMessage) {
        this.wUId = wUId;
        this.wMessage = wMessage;
    }

    public MessageModel(){

    }

    public String getwUId() {
        return wUId;
    }

    public void setwUId(String wUId) {
        this.wUId = wUId;
    }

    public String getwMessage() {
        return wMessage;
    }

    public void setwMessage(String wMessage) {
        this.wMessage = wMessage;
    }

    public String getwMessageId() {
        return wMessageId;
    }

    public void setwMessageId(String wMessageId) {
        this.wMessageId = wMessageId;
    }

    public Long getwTimestamp() {
        return wTimestamp;
    }

    public void setwTimestamp(Long wTimestamp) {
        this.wTimestamp = wTimestamp;
    }
}
