package com.example.librarymanagementsystem_users.api;

import com.example.librarymanagementsystem_users.models.LoginRequestDto;
import com.example.librarymanagementsystem_users.models.LoginResponseDto;
import com.example.librarymanagementsystem_users.models.UserRequestDto;
import com.example.librarymanagementsystem_users.models.UserResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("users") // change to your endpoint
    Call<List<UserResponseDto>> getUsers();

    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequest);

    @POST("signup")
    Call<Void> signup(@Body UserRequestDto signupRequest);

}
