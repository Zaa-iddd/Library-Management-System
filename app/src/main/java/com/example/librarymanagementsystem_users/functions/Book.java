package com.example.librarymanagementsystem_users.functions;

import java.io.Serializable;

public class Book implements Serializable {
    private long id;
    private String title;
    private String author;
    private int copies_available;
    private String cover_image_url;
    private String genre;
    private String language;
    private int number_of_pages;
    private String publication_date;
    private String publisher;
    private String status;
    private String summary;
    private int total_copies;

    public Book(long id, String author, int copies_available, String cover_image_url, String genre, String language, int number_of_pages, String publication_date, String publisher, String status, String summary, String title, int total_copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copies_available = copies_available;
        this.cover_image_url = cover_image_url;
        this.genre = genre;
        this.language = language;
        this.number_of_pages = number_of_pages;
        this.publication_date = publication_date;
        this.publisher = publisher;
        this.status = status;
        this.summary = summary;
        this.total_copies = total_copies;
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

    public int getCopies_available() {
        return copies_available;
    }

    public void setCopies_available(int copies_available) {
        this.copies_available = copies_available;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getNumber_of_pages() {
        return number_of_pages;
    }

    public void setNumber_of_pages(int number_of_pages) {
        this.number_of_pages = number_of_pages;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTotal_copies() {
        return total_copies;
    }

    public void setTotal_copies(int total_copies) {
        this.total_copies = total_copies;
    }
}
