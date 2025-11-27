package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

public class BookData {

    private static BookApiFunction api;


    public static void initialize(Context context) {
        api = new DatabaseBookApi();  // Use your backend or local DB
    }


    public static BookApiFunction getApi() {
        return api;
    }
}
