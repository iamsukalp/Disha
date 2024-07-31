package com.appsters.disha.Models;

public class ChatModal {

    String imageUrl;
    String name;
    String userId;

    public ChatModal(){}

    public ChatModal(String imageUrl, String name, String userId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
