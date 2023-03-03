package com.example.domain.repository


import com.example.domain.entity.DateStatistic
import com.example.domain.entity.RepoData

interface RepoRepository {

    suspend fun getRepoList(userName: String, pageNumber: Int): RepoData

    suspend fun getStatisticList(pageNumber: Int, userName: String, repoName: String): List<DateStatistic> // TODO enum Period, offset

}