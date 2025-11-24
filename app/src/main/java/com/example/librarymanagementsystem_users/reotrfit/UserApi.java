package com.example.librarymanagementsystem_users.reotrfit;


import com.example.librarymanagementsystem_users.models.LoginRequestDto;
import com.example.librarymanagementsystem_users.models.UserRequestDto;
import com.example.librarymanagementsystem_users.models.UserResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @GET("/users/get-all")
    Call<List<UserResponseDto>> getAllUsers();

    // Corrected the path to match the Spring Boot controller
    @POST("/users") 
    Call<UserResponseDto> save(@Body UserRequestDto userRequestDto);
    //login
    @POST("/users/login")
    Call<UserResponseDto> login(@Body LoginRequestDto dto);

}
