package com.example.data.api

import com.example.data.entities.api.ResponceRepositories
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubApi {

//    @POST("users/{user}/repos")
//    suspend fun getUserData(
//        @Path("user") name: String,
//        ): ArrayList<ResponceReposytories>

    @GET("users/{user}/repos")
    suspend fun getRepositoriesData (
        @Path("user") nameUser: String
    ): List<ResponceRepositories>

//    @GET("repos/{user}/{repository}")
//    fun getReposData (
//        @Path("user") userName: String,
//        @Path("repository") userRepository: String
//    ): Call<ArrayList<ResponceReposytories>>

}