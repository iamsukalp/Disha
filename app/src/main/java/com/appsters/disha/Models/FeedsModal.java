package com.appsters.disha.Models;

public class FeedsModal {

    String details;
    String header;
    String imageUrl;
    long created_at;

    public FeedsModal(){}

    public FeedsModal(String details, String header, String imageUrl, long created_at) {
        this.details = details;
        this.header = header;
        this.imageUrl = imageUrl;
        this.created_at = created_at;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
