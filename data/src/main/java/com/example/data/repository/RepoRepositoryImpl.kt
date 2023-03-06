package com.example.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.domain.entity.RepoData
import com.example.data.entities.RepoDateStatistic
import com.example.data.source.RemoteStarredBody
import com.example.domain.entity.DateStatistic
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
    private var currentDate = Date()
    private var indexDate = 0
    private var pageCount = 100

    override suspend fun getRepoList(userName: String, pageNumber: Int): RepoData {
        val repoList = getRepoRepositoryList(userName, pageNumber)
        pageCount = repoList[1].stargazers
        return RepoData(repoList, repoList.size)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStatisticList(
        period: RepoRepository.Period,
        displacement: Date,
        userName: String,
        repoName: String
    ): List<DateStatistic> {

        Log.d("displacement", displacement.toString())
        Log.d("displacementYear", displacement.getDateYear().toString())

        if (pageCount % 100 != 0) {
            pageCount = pageCount / 100 + 1
        } else pageCount /= 100

        if (allDateList.size != 0) {
            if (allDateList.first().starredAt.getDateYear() < displacement.getDateYear()) {
                println("nothing")
            } else {
                while (allDateList.first().starredAt.getDateYear() >= displacement.getDateYear()) {
                    if (pageCount != 0) {
                        pageCount -= 1
                        val starred = getStarredList(userName, repoName, pageCount)
                        allDateList.addAll(starred)

                    }
                }
            }
        } else {
            val starredList = getStarredList(userName, repoName, pageCount)
            allDateList.addAll(starredList)
            Log.d("allDateListFirst", allDateList.first().starredAt.getDateYear().toString())
            while (allDateList.first().starredAt.getDateYear() >= displacement.getDateYear()) {
                if (pageCount != 0) {
                    pageCount -= 1
                    val starred = getStarredList(userName, repoName, pageCount)
                    allDateList.addAll(starred)
                }
            }
        }

        Log.d("allDateListRepo", allDateList.toString())

        val dayOfWeek = Calendar.MONDAY
        val now = Calendar.getInstance()
        now.set(
            currentDate.getDateYear(),
            currentDate.getDateMonth(),
            currentDate.getDateDayOfMonth()
        )
        val weekday = now[Calendar.DAY_OF_WEEK]
        val days = (LocalDate.now().dayOfYear - weekday + dayOfWeek) % 7
        now.add(Calendar.DAY_OF_YEAR, days)
        val weekStart = now.time
        now.add(Calendar.DAY_OF_YEAR, 6)
        val weekEnd = now.time
        val periodWeek: ClosedRange<Date> = (weekStart..weekEnd)

        if (period == RepoRepository.Period.YEAR) {
            structureDateList.clear()

            for (month in 0..11) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == displacement.getDateYear() &&
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

            for (week in 0..4) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == displacement.getDateYear() &&
                        allDateList[date].starredAt.getDateMonth() == displacement.getDateMonth() &&
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

            for (day in 0..6) {
                val list = mutableListOf<User>()
                for (date in allDateList.indices) {
                    if ((allDateList[date].starredAt.getDateYear()) == displacement.getDateYear() &&
                        allDateList[date].starredAt.getDateMonth() == displacement.getDateMonth() &&
                        allDateList[date].starredAt in periodWeek && allDateList[date].starredAt.getDateDayOfMonth() == day
                    ) {
                        list.add(allDateList[date].user)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateList[indexDate].starredAt, list))
            }

        }

        Log.d("RepoRepositoryImpl", structureDateList.toString())
        return structureDateList
    }


}