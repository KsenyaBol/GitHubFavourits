package com.example.domain.repository


import com.example.domain.entity.DateStatistic
import com.example.domain.entity.RepoData
import java.util.Date

interface RepoRepository {

    enum class Period {
        WEEK, MONTH, YEAR
    }

    suspend fun getRepoList(userName: String, pageNumber: Int): RepoData

    suspend fun getStatisticList
                (period: Period,
                 displacement: Date,
                 userName: String,
                 repoName: String
    ): List<DateStatistic>

}