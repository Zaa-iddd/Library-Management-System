package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;

public class BorrowedBook implements Serializable {

    private long databaseId;
    private String title;
    private String dueDate;
    private String cover_image_url;

    public BorrowedBook(long databaseId, String title, String dueDate, String cover_image_url) {
        this.databaseId = databaseId;
        this.title = title;
        this.dueDate = dueDate;
        this.cover_image_url = cover_image_url;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }
}
