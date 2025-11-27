package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;

public class RequestedBook implements Serializable {

    private long id;
    private String title;
    private String author;
    private String status;
    private String cover_image_url;

    public RequestedBook(long id, String title, String author, String status, String cover_image_url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.cover_image_url = cover_image_url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }
}
