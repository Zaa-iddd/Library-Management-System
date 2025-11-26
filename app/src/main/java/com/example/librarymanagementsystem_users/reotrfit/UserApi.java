package com.example.librarymanagementsystem_users.reotrfit;

import com.example.librarymanagementsystem_users.models.LoginRequestDto;
import com.example.librarymanagementsystem_users.models.UserProfileDto;
import com.example.librarymanagementsystem_users.models.UserRequestDto;
import com.example.librarymanagementsystem_users.models.UserResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/users/get-all")
    Call<List<UserResponseDto>> getAllUsers();

    @POST("/users")
    Call<UserResponseDto> save(@Body UserRequestDto userRequestDto);

    // Android users only
    @POST("/users/userLogin")
    Call<UserResponseDto> userLogin(@Body LoginRequestDto dto);

    @PUT("/users/{id}/profile")
    Call<UserResponseDto> updateUserProfile(
            @Path("id") Long id,
            @Body UserProfileDto dto
    );

    @GET("/users/{id}")
    Call<UserResponseDto> getUserById(@Path("id") Long id);
}
