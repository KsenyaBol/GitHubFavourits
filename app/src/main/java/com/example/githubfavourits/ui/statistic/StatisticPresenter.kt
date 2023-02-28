package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.data.entities.RepoDateStatistic
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.User
import com.example.githubfavourits.ui.base.BasePresenter
import com.omega_r.libs.extensions.date.getDateDayOfMonth
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StatisticPresenter(private val nameUser: String, private val repo: Repo) :
    BasePresenter<StatisticView>() {

    private val allDateBarList = arrayListOf<DateStatistic>()
    private var structureDateList = arrayListOf<DateStatistic>()
    private var direction = Period.YEAR
    private val currentDate = Date()
    private var dateFormatForDay: DateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private var year: Int = SimpleDateFormat("yyyy", Locale.getDefault()).format(currentDate).toInt()
    private var month = SimpleDateFormat("MM", Locale.getDefault()).format(currentDate).toInt()
    private var day = SimpleDateFormat("dd", Locale.getDefault()).format(currentDate).toInt()
    private var dayOfYear = 0
    private var weekStartGlobal = "day"
    private var weekEndGlobal = "day"
    private var indexDate = 0
    private var pageNumber = repo.stargazers
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun getStarredDataList() {
        launch {

            val nameRepo = repo.name

            if (pageNumber % 100 != 0) {
                pageNumber = pageNumber / 100 + 1
            } else pageNumber /= 100

            val starredAtList = repoRepository.getStatisticList(pageNumber, nameUser, nameRepo)
            allDateBarList.addAll(starredAtList)

            viewState.allDateBarList = allDateBarList

            Log.d("allDateBarList", allDateBarList.toString())
            Log.d("starredAtList", starredAtList.toString())

        }

    }

    fun getStarredListRepeatedly() {
        val nameRepo = repo.name
        pageNumber -= 1

        launch {
            val starredAtList = repoRepository.getStatisticList(pageNumber, nameUser, nameRepo)
            allDateBarList.addAll(starredAtList)

            viewState.allDateBarList = allDateBarList
        }

    }

    fun onPeriodClicked(direction: Period) {
        viewState.direction = direction
    }

    fun onPreviousClicked() {

        if (direction == Period.YEAR) {
            year -= 1
        }

        if (direction == Period.MONTH) {
            val monthNow = Calendar.MONTH
            val yearNow = Calendar.YEAR
            if (month == 1) {
                month = 13
                year -= 1
            }
            if (month > monthNow || year > yearNow) {
                month -= 1
            }

        }

        if (direction == Period.WEEK) {
            val dayNow = Calendar.DAY_OF_MONTH
            val monthNow = Calendar.MONTH
            val yearNow = Calendar.YEAR
            if (day <= 7) {
                if (month == 1) {
                    month = 13
                    year -= 1
                }
                month -= 1
                day = 35
            }
            if (month > monthNow || year > yearNow) {
                day -= 7
                dayOfYear -= 7
            }

        }

        viewState.day = day
        viewState.dayOfYear = dayOfYear
        viewState.month = month
        viewState.year = year
    }

    fun onNextClicked() {

        if (direction == Period.YEAR) {
            val yearNow = Calendar.YEAR
            if (year >= yearNow) {
                year += 1
            }

        }

        if (direction == Period.MONTH) {
            val monthNow = Calendar.MONTH
            val yearNow = Calendar.YEAR
            if (month == 12) {
                month = 0
                year += 1
            }
            if (month > monthNow || year > yearNow) {
                month += 1
            }

        }

        if (direction == Period.WEEK) {
            val dayNow = Calendar.DAY_OF_MONTH
            val monthNow = Calendar.MONTH
            val yearNow = Calendar.YEAR
            if (day >= 24) {
                if (month == 12) {
                    month = 0
                    year += 1
                }
                month += 1
                day = -6
            }
            if (day > dayNow || month > monthNow || year > yearNow) {
                day += 7
                dayOfYear += 7
            }

        }

        viewState.day = day
        viewState.dayOfYear = dayOfYear
        viewState.month = month
        viewState.year = year
    }

    fun getDateCount() {
        val dayOfWeek = Calendar.MONDAY
        val now = Calendar.getInstance()
        now.set(year, month, day)
        val weekday = now[Calendar.DAY_OF_WEEK]
        val days = (dayOfYear - weekday + dayOfWeek) % 7
        now.add(Calendar.DAY_OF_YEAR, days)
        val weekStart = now.time
        now.add(Calendar.DAY_OF_YEAR, 6)
        val weekEnd = now.time
        val period: ClosedRange<Date> = (weekStart..weekEnd)

        weekStartGlobal = dateFormatForDay.format(weekStart)
        weekEndGlobal = dateFormatForDay.format(weekEnd)

        structureDateList.clear()

        if (direction == Period.YEAR) {

            for (month in 0..11) {
                val list = arrayListOf<User>()
                for (date in allDateBarList.indices) {
                    if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() ==
                        month) {
                        list.addAll(allDateBarList[date].userList)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateBarList[indexDate].starredAt, list))
            }

        }

        if (direction == Period.MONTH) {

            for (week in 0..4) {
                val list = arrayListOf<User>()
                for (date in allDateBarList.indices) {
                    if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() ==
                        month && (allDateBarList[date].starredAt.getDateDayOfMonth() / 7 - 1) == week
                    ) {
                        list.addAll(allDateBarList[date].userList)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateBarList[indexDate].starredAt, list))
            }

        }

        if (direction == Period.WEEK) {

            for (day in 0..6) {
                val list = arrayListOf<User>()
                for (date in allDateBarList.indices) {
                    if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() == month &&
                        allDateBarList[date].starredAt in period && allDateBarList[date].starredAt.day == day
                    ) {
                        list.addAll(allDateBarList[date].userList)
                        indexDate = date
                    }
                }
                structureDateList.add(RepoDateStatistic(allDateBarList[indexDate].starredAt, list))
            }

        }

        viewState.structureDateList = structureDateList
        viewState.weekStartGlobal = weekStartGlobal
        viewState.weekEndGlobal = weekEndGlobal

    }


    enum class Period {
        WEEK, MONTH, YEAR
    }


}