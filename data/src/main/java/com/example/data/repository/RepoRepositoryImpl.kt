package com.example.data.repository

import android.util.Log
import com.example.data.entities.RepoDateStatistic
import com.example.data.source.RemoteStarredBody
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
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

    override suspend fun getStatisticList(userName: String, repoName: String): List<DateStatistic> {
        val starredList = getStarredList(userName, repoName)
        //https://api.github.com/repos/google/github/stargazers?per_page=100
        //stargazers_count

        Log.d("starredList1", starredList.toString())
        allDateList = starredList

        val list = arrayListOf<User>()
        for (date in allDateList.indices) {
            for (nextDate in (date + 1) until allDateList.size) {
                    val i = dateFormat.format(allDateList[date].starredAt)
                    val j = dateFormat.format(allDateList[nextDate].starredAt)

                    if (i == j) {
                        list.add(allDateList[date].userList)
                    }
            }
            structureDateList.add(RepoDateStatistic(allDateList[date].starredAt, list))

        }

        Log.d("structureDateList", structureDateList.toString())
        return structureDateList
    }

}