package com.example.librarymanagementsystem_users.reotrfit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    //  PC's local network IP (same Wi-Fi)
                    .baseUrl("http://192.168.1.4:8080/")
                    //trailing slash is important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UserApi getUserApi() {
        return getRetrofitInstance().create(UserApi.class);
    }

    public static BookApi getBookApi() {
        return getRetrofitInstance().create(BookApi.class);
    }
}
