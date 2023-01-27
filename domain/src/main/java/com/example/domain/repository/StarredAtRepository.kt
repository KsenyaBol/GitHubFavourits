package com.example.domain.repository

import com.example.domain.entity.StarredRepository

interface StarredAtRepository {

    suspend fun getStarredList(userName: String, repoName: String): List<StarredRepository>

}