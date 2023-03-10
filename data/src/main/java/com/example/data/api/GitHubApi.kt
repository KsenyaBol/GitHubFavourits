package com.example.data.api

import com.example.data.source.RemoteStarredBody
import com.example.data.source.RemoteRepoBody
import retrofit2.http.*


interface GitHubApi {

    @GET("users/{user}/repos?per_page=100")
    suspend fun getRepositoriesData(
        @Path("user") nameUser: String,
        @Query("page") pageNumber: Int,
    ): List<RemoteRepoBody>

    @Headers( "Accept: application/vnd.github.v3.star+json")
    @GET("repos/{user}/{repository}/stargazers?per_page=100")
    suspend fun getStarredAtData(
        @Path("user") userName: String,
        @Path("repository") repositoryName: String,
        @Query("page") pageNumber: Int,
    ): List<RemoteStarredBody>

}