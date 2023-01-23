package com.example.data.api

import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubApi {

//    @POST("users/{user}/repos")
//    suspend fun getUserData(
//        @Path("user") name: String,
//        ): ArrayList<ResponceReposytories>

    @GET("users/{user}/repos")
    fun getUserData (
        @Path("user") nameUser: String
    ): Call<ArrayList<ResponseUser>>

    @GET("repos/{user}/{repository}")
    fun getReposData (
        @Path("user") userName: String,
        @Path("repository") userRepository: String
    ): Call<ArrayList<ResponceReposytories>>

}