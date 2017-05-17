package edu.sjsu.cmpe.fourhorsemen.connectivity.beans;

/**
 * Created by keyurgolani on 5/15/17.
 */

public class Message {
    private int messageID;
    private Profile from;
    private Profile to;
    private String subject;
    private String content;
    private String timestamp;

    public Message(int messageID, Profile from, Profile to, String subject, String content, String timestamp) {
        this.messageID = messageID;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Profile getFrom() {
        return from;
    }

    public void setFrom(Profile from) {
        this.from = from;
    }

    public Profile getTo() {
        return to;
    }

    public void setTo(Profile to) {
        this.to = to;
    }
}
