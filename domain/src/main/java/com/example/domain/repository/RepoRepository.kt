package com.example.domain.repository

import com.example.domain.entity.Repo
import com.example.domain.entity.StarredRepository

interface RepoRepository {

    // TODO: rename getDataStatistics, add group enum, current shift

    suspend fun getRepoList(userName: String): List<Repo>

    suspend fun getStarredList(userName: String, repoName: String): List<StarredRepository>
}