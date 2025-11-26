package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

public class BookData {

    private static BookApiFunction api;

    /**
     * Initializes the Book API.
     * Call this once in MainActivity or Application class.
     */
    public static void initialize(Context context) {
        api = new DatabaseBookApi();  // Use your backend or local DB
    }

    /**
     * Returns the active API instance.
     */
    public static BookApiFunction getApi() {
        return api;
    }
}
