package com.keithmaher.autismfriendlylocations.models;


import java.util.UUID;

public class News extends Location {

    public String newsId;
    public String newsName;
    public String newsMain;
    public String newsDate;
    public String newsImg;
    public String newsLocation;

    public News() {
    }

    public News(String newsName, String newsDate, String newsImg, String newsLocation) {
        this.newsId = UUID.randomUUID().toString();
        this.newsName = newsName;
        this.newsDate = newsDate;
        this.newsImg = newsImg;
        this.newsLocation = newsLocation;
    }

    public String getNewsLocation() {
        return newsLocation;
    }

    public void setNewsLocation(String newsLocation) {
        this.newsLocation = newsLocation;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getNewsMain() {
        return newsMain;
    }

    public void setNewsMain(String newsMain) {
        this.newsMain = newsMain;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }
}
