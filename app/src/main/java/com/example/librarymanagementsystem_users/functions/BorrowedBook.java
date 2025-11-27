package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;

public class BorrowedBook implements Serializable {

    private long databaseId;
    private String title;
    private String dueDate;

    public BorrowedBook(long databaseId, String title, String dueDate) {
        this.databaseId = databaseId;
        this.title = title;
        this.dueDate = dueDate;
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
}
