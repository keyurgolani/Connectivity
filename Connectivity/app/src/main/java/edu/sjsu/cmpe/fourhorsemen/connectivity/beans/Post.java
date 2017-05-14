package edu.sjsu.cmpe.fourhorsemen.connectivity.beans;

import java.util.Date;

/**
 * Created by gauravchodwadia on 5/13/17.
 */

public class Post {

    private String userFullName;
    private String userPhoto;
    private String content;
    private Date timestamp;

    public Post(String userFullName, String userPhoto, String content, Date timestamp) {
        this.userFullName = userFullName;
        this.userPhoto = userPhoto;
        this.content = content;
        this.timestamp = timestamp;
    }
}
