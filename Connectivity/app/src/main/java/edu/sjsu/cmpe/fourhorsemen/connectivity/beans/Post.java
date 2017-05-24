package edu.sjsu.cmpe.fourhorsemen.connectivity.beans;

import android.util.Log;


/**
 * Created by gauravchodwadia on 5/13/17.
 */

public class Post {

    // TODO: Use Profile bean for all the profile details and make relevant changes to Controller logic. - Keyur
    private int postID;
    private String postPhoto;
    private String content;
    private String timestamp;
    private Profile from;

    public Post(){

    }

    public Post(int postID, String postPhoto, String content, Profile from, String timestamp) {
        this.postID = postID;
        this.postPhoto = postPhoto;
        this.content = content;
        this.from = from;
        this.timestamp = timestamp;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public Profile getFrom() {
        return from;
    }

    public void setFrom(Profile from) {
        this.from = from;
    }

    public void setPostPhoto(String postPhoto) {
        this.postPhoto = postPhoto;
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

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
}
