package com.example.librarymanagementsystem_users.reotrfit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit = null;

    // para dae paulit uit
    public RetrofitService() {}

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    // Use 10.0.2.2 pag emulator pag dae yug ip mo
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Public static method to get the UserApi service
    public static UserApi getUserApi() {
        return getRetrofitInstance().create(UserApi.class);
    }
}
