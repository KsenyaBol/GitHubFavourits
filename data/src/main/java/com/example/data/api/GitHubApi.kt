package com.example.data.api

import com.example.data.entities.api.ResponceReposytories
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
        @Path("user") name: String
    ): Call<ArrayList<ResponceReposytories>>


}