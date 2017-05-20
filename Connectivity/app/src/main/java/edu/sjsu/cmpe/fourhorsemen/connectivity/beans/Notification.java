package edu.sjsu.cmpe.fourhorsemen.connectivity.beans;

/**
 * Created by keyurgolani on 5/19/17.
 */

public class Notification {
    private int notification_id;
    private int profile;
    private int friend;
    private String text;
    private String timestamp;

    public Notification(int notification_id, int profile, int friend, String text, String timestamp) {
        this.notification_id = notification_id;
        this.profile = profile;
        this.friend = friend;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
