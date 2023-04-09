package com.example.myapplication.api

import com.example.myapplication.response.DetailUserResponse
import com.example.myapplication.response.ItemsItem
import com.example.myapplication.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserProfile(
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/{endpoint}")
    fun getUserConnectionResponse(
        @Path("username") username: String,
        @Path("endpoint") endpoint: String
    ): Call<List<ItemsItem>>
}