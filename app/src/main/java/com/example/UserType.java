package com.example;

public class UserType {
    String userId;
    String profilePicUrl;
    String userName;

    public UserType() {
    }

    public UserType(String userId, String userName, String profilePicUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "userId='" + userId + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
