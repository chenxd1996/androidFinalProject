package com.example.dell.afinal;

/**
 * Created by roger on 2018/1/7.
 */

public class Diary {
    private String title;
    private String content;
    private String img_url;
    private String emotion;
    private String date;

    public Diary(String title, String content, String img_url, String emotion, String date) {
        this.title = title;
        this.content = content;
        this.img_url = img_url;
        this.emotion = emotion;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getEmotion() {
        return emotion;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
