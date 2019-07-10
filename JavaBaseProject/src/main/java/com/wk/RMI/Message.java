package com.wk.RMI;

import java.io.Serializable;

public class Message implements Serializable{

    private String messageText;
    private String contextType;

    public Message() {
    }

    public Message(String messageText, String contextType) {
        this.messageText = messageText;
        this.contextType = contextType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageText='" + messageText + '\'' +
                ", contextType='" + contextType + '\'' +
                '}';
    }
}
