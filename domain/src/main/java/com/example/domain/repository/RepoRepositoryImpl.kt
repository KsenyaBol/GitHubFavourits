package com.example.domain.repository

import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.User
import java.util.*

class RepoRepositoryImpl(override var currentDate: Date) : RepoRepository {

    enum class DateValue() {
        WEEK, MONTH, YEAR
    }
    var allDateList = listOf<DateStatistic>()

    override suspend fun getRepoList(userName: String): List<Repo> {
        TODO("Not yet implemented")
    }

    override suspend fun getStarredList(userName: String, repoName: String): List<DateStatistic> {
        val starredAtList = getStarredList(userName, repoName)
        allDateList = starredAtList
        val list = arrayListOf<User>()
        allDateList.forEach { date ->
            list.addAll(date.userList)
        }
    }
}