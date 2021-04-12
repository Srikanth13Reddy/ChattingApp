package com.apptomate.chattingapp.models;

public class ModelChatNew {

    String message,receiver,sender,type;
    String seenMessage;
    Long timeStamp;

    public ModelChatNew() {
    }

    public ModelChatNew(String message, String receiver, String sender, Long timeStamp, String seenMessage,String type) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.seenMessage = seenMessage;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSeenMessage() {
        return seenMessage;
    }

    public void setSeenMessage(String seenMessage) {
        this.seenMessage = seenMessage;
    }
}
