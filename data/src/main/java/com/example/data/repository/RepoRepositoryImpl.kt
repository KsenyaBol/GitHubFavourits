package com.example.data.repository

import android.util.Log
import com.example.data.entities.RepoDateStatistic
import com.example.data.source.RemoteStarredBody
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.User
import com.example.domain.repository.RepoRepository
import com.omega_r.base.errors.ErrorHandler
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RepoRepositoryImpl(errorHandler: ErrorHandler, dataRepoSource: DataRepoSource) :
    RepoRepository, DataRepoOmegaRepository(errorHandler, dataRepoSource) {

    private var allDateList = listOf<RemoteStarredBody>()
    private var structureDateList = arrayListOf<DateStatistic>()
    private var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override suspend fun getStatisticList(pageNumber: Int, userName: String, repoName: String): List<DateStatistic> {

        var page = pageNumber
        val starredList = getStarredList(page, userName, repoName)
        //https://api.github.com/repos/google/github/stargazers?per_page=100
        //stargazers_count

        allDateList = starredList


        for (date in allDateList.indices) {
            val list = arrayListOf<User>()
            list.clear()
            list.add(allDateList[date].userList)

            structureDateList.add(RepoDateStatistic(allDateList[date].starredAt, list))

        }

        Log.d("allDateList", allDateList.toString())
        Log.d("strList", structureDateList.toString())
        return structureDateList
    }

}