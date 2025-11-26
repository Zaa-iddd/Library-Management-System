package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;

public class RequestedBook implements Serializable {

    private String title;
    private String status;

    public RequestedBook(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
