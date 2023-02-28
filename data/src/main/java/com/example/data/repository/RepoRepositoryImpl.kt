package com.example.data.repository

import android.util.Log
import com.example.domain.entity.RepoData
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

    override suspend fun getRepoList(userName: String, pageNumber: Int): RepoData {
        val repoList = getRepoRepositoryList(userName, pageNumber)
        return RepoData(repoList, repoList.size)
    }

    override suspend fun getStatisticList(pageNumber: Int, userName: String, repoName: String): List<DateStatistic> {

        val starredList = getStarredList(userName, repoName, pageNumber)

        allDateList = starredList

        getRepoList(userName, pageNumber)


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