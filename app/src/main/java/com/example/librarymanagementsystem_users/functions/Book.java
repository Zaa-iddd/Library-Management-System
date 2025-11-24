package com.example.librarymanagementsystem_users.functions;

import java.util.Objects;

public class Book extends LibraryItem {
    private int coverResourceId;
    private String description;
    private String genre;

    public Book(String title, String author, int coverResourceId, String description, String genre) {
        super(title, author);
        this.coverResourceId = coverResourceId;
        this.description = description;
        this.genre = genre;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return coverResourceId == book.coverResourceId && Objects.equals(description, book.description) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coverResourceId, description, genre);
    }

    @Override
    public void displayInfo() {

    }
}
