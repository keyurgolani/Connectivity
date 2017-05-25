package edu.sjsu.cmpe.fourhorsemen.connectivity.beans;

import java.util.Date;

/**
 * Created by keyurgolani on 5/15/17.
 */

public class Profile {
    private int profile;
    private int account;
    private String f_name;
    private String l_name;
    private String profile_pic;
    private Location location ;
    private String profession;
    private String screen_name;
    private Date dob;
    private String gender;
    private String timestamp;

    public Profile() {
    }

    public Profile(int profile, String profile_pic, String screen_name) {
        this.profile = profile;
        this.profile_pic = profile_pic;
        this.screen_name = screen_name;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profile=" + profile +
                ", account=" + account +
                ", f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
//                ", profile_pic='" + profile_pic + '\'' +
                ", location=" + location +
                ", profession='" + profession + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
