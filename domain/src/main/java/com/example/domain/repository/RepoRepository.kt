package com.example.domain.repository


import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import java.util.*

interface RepoRepository {

    suspend fun getRepoList(userName: String): List<Repo>

    suspend fun getStatisticList(userName: String, repoName: String): List<DateStatistic>


}