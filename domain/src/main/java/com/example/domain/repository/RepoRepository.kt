package com.example.domain.repository


import com.example.domain.entity.DateStatistic
import com.example.domain.entity.RepoData

interface RepoRepository {

    enum class Period {
        WEEK, MONTH, YEAR
    }

    suspend fun getRepoList(userName: String, pageNumber: Int): RepoData

    suspend fun getStatisticList
                (period: Period,
                 displacement: Int,
                 userName: String,
                 repoName: String
    ): List<DateStatistic>

}