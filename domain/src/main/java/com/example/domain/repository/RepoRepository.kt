package com.example.domain.repository


import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.RepoData
import java.util.*

interface RepoRepository {

    suspend fun getRepoList(userName: String, pageNumber: Int): RepoData

    suspend fun getStatisticList(pageNumber: Int, userName: String, repoName: String): List<DateStatistic>

}