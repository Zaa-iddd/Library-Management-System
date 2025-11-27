package com.example.librarymanagementsystem_users.functions;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Book implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("copiesAvailable")  // backend uses camelCase? (check)
    private int copies_available;

    @SerializedName("coverImageUrl")  // THIS IS CORRECT
    private String cover_image_url;

    @SerializedName("genre")
    private String genre;

    @SerializedName("language")
    private String language;

    @SerializedName("numberOfPages")
    private int number_of_pages;

    @SerializedName("publicationDate")
    private String publication_date;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("status")
    private String status;

    @SerializedName("summary")
    private String summary;

    @SerializedName("totalCopies")
    private int total_copies;

    public Book() {}

    // ---------- GETTERS ----------
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getCopies_available() { return copies_available; }

    public String getCover_image_url() {
        if (cover_image_url != null && !cover_image_url.startsWith("http")) {
            return "https://pjhjpxgexmagumhdeocm.supabase.co/storage/v1/object/public/" + cover_image_url;
        }
        return cover_image_url;
    }

    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public int getNumber_of_pages() { return number_of_pages; }
    public String getPublication_date() { return publication_date; }
    public String getPublisher() { return publisher; }
    public String getStatus() { return status; }
    public String getSummary() { return summary; }
    public int getTotal_copies() { return total_copies; }
}
