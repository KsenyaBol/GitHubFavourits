package com.example.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.domain.entity.RepoData
import com.example.data.entities.RepoDateStatistic
import com.example.data.source.RemoteStarredBody
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.User
import com.example.domain.repository.RepoRepository
import com.omega_r.base.errors.ErrorHandler
import com.omega_r.libs.extensions.date.getDateDayOfMonth
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class RepoRepositoryImpl(errorHandler: ErrorHandler, dataRepoSource: DataRepoSource) :
    RepoRepository, DataRepoOmegaRepository(errorHandler, dataRepoSource) {

    private var allDateList = mutableListOf<RemoteStarredBody>()
    private var structureDateList = mutableListOf<DateStatistic>()
    private var allRepoList = mutableListOf<Repo>()
    private var currentDate = Date()
    private var year: Int = currentDate.getDateYear()
    private var month = currentDate.getDateMonth()
    private var dayNow = currentDate.getDateDayOfMonth()
    private var indexDate = 0
    private var pageCount = 1000
    private var count = 0
    private var pageNumber = 1
    private var queryCount = 0

    override suspend fun getRepoList(userName: String, pageNumber: Int): RepoData {
        val repoList = getRepoRepositoryList(userName, pageNumber)
        allRepoList.addAll(repoList)
        this.pageNumber = pageNumber
        queryCount += 1
        return RepoData(repoList, repoList.size)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStatisticList(
        period: RepoRepository.Period,
        displacement: Int,
        userName: String,
        repoName: String
    ): List<DateStatistic> {

        Log.d("RepoDisplacement", displacement.toString())

        allRepoList.forEachIndexed { index, repo ->
            if(repo.name == repoName) pageCount = allRepoList[index].stargazers
        }

        if (pageCount % 100 != 0) {
            pageCount = pageCount / 100 + 1
        } else pageCount /= 100

        if (allDateList.size == 0) {
            val starredList = getStarredList(userName, repoName, pageNumber)
            allDateList.addAll(starredList)
            if (displacement > 100) {
                year = displacement
            }
            while (allDateList.first().starredAt.getDateYear() >= year && pageNumber > 0) {
                pageNumber -= 1
                val starred = getStarredList(userName, repoName, pageNumber)
                allDateList.addAll(starred)
            }
        } else {
            if (displacement > 100) {
                year = displacement
            }
            if (allDateList.first().starredAt.getDateYear() >= year) {
                while (allDateList.first().starredAt.getDateYear() >= year && pageNumber > 0) {
                    pageNumber -= 1
                    val starred = getStarredList(userName, repoName, pageNumber)
                    allDateList.addAll(starred)
                }
            }
        }

        count += 1

        Log.d("pageCountRepoRepos", pageCount.toString())
        Log.d("allDateListRepo", allDateList.toString())

        val dayOfWeek = Calendar.MONDAY
        val now = Calendar.getInstance()
        now.set(year, month, dayNow)
        val weekday = now[Calendar.DAY_OF_WEEK]
        val days = (LocalDate.now().dayOfYear - weekday + dayOfWeek) % 7
        now.add(Calendar.DAY_OF_YEAR, days)
        val weekStart = now.time
        now.add(Calendar.DAY_OF_YEAR, 6)
        val weekEnd = now.time
        val periodWeek: ClosedRange<Date> = (weekStart..weekEnd)

        if (period == RepoRepository.Period.YEAR) {
            structureDateList.clear()
            year = displacement

            for (month in 0..11) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == year &&
                        allDateList[date].starredAt.getDateMonth() == month
                    ) {
                        list.add(allDateList[date].user)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateList[indexDate].starredAt, list))
            }

        }

        if (period == RepoRepository.Period.MONTH) {
            structureDateList.clear()
            month = displacement

            for (week in 0..4) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == year &&
                        allDateList[date].starredAt.getDateMonth() == month &&
                        (allDateList[date].starredAt.getDateDayOfMonth() / 7 - 1) == week
                    ) {
                        list.add(allDateList[date].user)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateList[indexDate].starredAt, list))
            }

        }

        if (period == RepoRepository.Period.WEEK) {
            structureDateList.clear()
            dayNow = displacement

            for (day in 0..6) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == year &&
                        allDateList[date].starredAt.getDateMonth() == month &&
                        allDateList[date].starredAt in periodWeek && allDateList[date].starredAt.getDateDayOfMonth() == day
                    ) {
                        list.add(allDateList[date].user)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateList[indexDate].starredAt, list))
            }

        }

        Log.d("queryCount", queryCount.toString())
        Log.d("countRepo", count.toString())
        Log.d("RepoRepositoryImpl", structureDateList.toString())
        return structureDateList
    }




}