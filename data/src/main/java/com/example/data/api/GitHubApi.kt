package com.example.data.api

import com.example.data.entities.RemoteRepoBody
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubApi {

    @GET("users/{user}/repos")
    suspend fun getRepositoriesData(
        @Path("user") nameUser: String
    ): List<RemoteRepoBody>

    @GET("repos/{user}/{repository}")
    suspend fun getStarValue(
        @Path("user") userName: String,
        @Path("repository") repositoryName: String
    ): List<RemoteFavouriteBody>

}