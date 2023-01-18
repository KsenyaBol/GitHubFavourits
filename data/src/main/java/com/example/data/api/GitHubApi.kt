package com.example.data.api

import com.example.data.entities.api.ResponseUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GitHubApi {

    @GET("https://api.github.com/user")
    suspend fun getUserData(
        @Header("user") name: String,
        @Query("fields") fields: String
    ): ResponseUser

}