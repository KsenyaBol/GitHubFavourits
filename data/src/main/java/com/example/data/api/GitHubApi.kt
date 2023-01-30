package com.example.data.api

import com.example.data.entities.RemoteStarredBody
import com.example.data.entities.RemoteRepoBody
import retrofit2.http.*


interface GitHubApi {

    @GET("users/{user}/repos")
    suspend fun getRepositoriesData(
        @Path("user") nameUser: String
    ): List<RemoteRepoBody>

    @Headers( "Accept: application/vnd.github.v3.star+json")
    @GET("repos/{user}/{repository}/stargazers")
    suspend fun getStarredAtData(
        @Path("user") userName: String,
        @Path("repository") repositoryName: String
    ): List<RemoteStarredBody>

}