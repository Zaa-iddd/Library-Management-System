package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;
import java.util.Objects;

public class Book extends LibraryItem implements Serializable {
    private int coverResourceId;
    private String description;
    private String genre;
    private boolean isTrending;
    private boolean isFavorite;

    public Book(String title, String author, int coverResourceId, String description, String genre, boolean isTrending, boolean isFavorite) {
        super(title, author);
        this.coverResourceId = coverResourceId;
        this.description = description;
        this.genre = genre;
        this.isTrending = isTrending;
        this.isFavorite = isFavorite;
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

    public boolean isTrending() {
        return isTrending;
    }

    public void setTrending(boolean trending) {
        isTrending = trending;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return coverResourceId == book.coverResourceId && isTrending == book.isTrending && isFavorite == book.isFavorite && Objects.equals(description, book.description) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coverResourceId, description, genre, isTrending, isFavorite);
    }

    @Override
    public void displayInfo() {

    }
}
