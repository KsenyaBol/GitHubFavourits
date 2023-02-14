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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StatisticPresenter(private val nameUser: String, private val repo: Repo) : BasePresenter<StatisticView>() {

    private val allDateBarList = arrayListOf<DateStatistic>()
    private var structureDateList = arrayListOf<DateStatistic>()
//    private var direction = Entity.YEAR
    private val currentDate = Date()
    private var dateFormatForYear: DateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private var dateFormatForMonth: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
    private var dateFormatForDay: DateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private var year: Int = dateFormatForYear.format(currentDate).toInt()
    private var month = dateFormatForMonth.format(currentDate).toInt()
    private var day = dateFormatForDay.format(currentDate).toInt()
    private var dayOfYear = 0
    private var weekStartGlobal = "day"
    private var weekEndGlobal = "day"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun getStarredDataList() {
        launchWithWaiting {

            val nameRepo = repo.name
            val starredAtList = repoRepository.getStarredList(nameUser, nameRepo)

//            val firstStar = dateFormatForYear.format(starredAtList)[0].toString().toInt()
//            val lastStar = dateFormatForYear.format(starredAtList).last().toString().toInt()
//                for (year in firstStar.. lastStar) {
//                    val list = arrayListOf<User>()
//                    starredAtList.forEach { starred ->
//
//                        if (starred.starredAt.getDateYear() == year) {
//                            list.addAll(starred.userList)
//                        }
//                    }
//                    val now = Date()
//
//
//                    allDateBarList.add(RepoDateStatistic(year, list))
//
//                }

//            val yearGroup = starredAtList.groupBy { it.favouriteAt -> year }
            val yearGroup = starredAtList.groupingBy { it.starredAt.getDateYear() }

            viewState.allDateBarList = allDateBarList

            Log.d("yerGroup", yearGroup.toString())
            Log.d("allDateBarList", allDateBarList.toString())
            Log.d("starredAtList", starredAtList.toString())

        }


    }

//    fun yearButtonClicked() {
//        direction = Entity.YEAR
//        viewState.direction = direction
//    }
//
//    fun monthButtonClicked() {
//        direction = Entity.MONTH
//        viewState.direction = direction
//    }
//
//    fun weekButtonClicked() {
//        direction = Entity.WEEK
//        viewState.direction = direction
//    }
//
//    fun backImageButtonClick() {
//
//        if (direction == Entity.YEAR) {
//            year -= 1
//        }
//
//        if (direction == Entity.MONTH) {
//            val monthNow = Calendar.MONTH
//            val yearNow = Calendar.YEAR
//            if (month == 1) {
//                month = 13
//                year -= 1
//            }
//            if (month > monthNow || year > yearNow) {
//                month -= 1
//            }
//
//        }
//
//        if (direction == Entity.WEEK) {
//            val dayNow = Calendar.DAY_OF_MONTH
//            val monthNow = Calendar.MONTH
//            val yearNow = Calendar.YEAR
//            if (day <= 7) {
//                if (month == 1) {
//                    month = 13
//                    year -= 1
//                }
//                month -= 1
//                day = 35
//            }
//            if (day > dayNow || month > monthNow || year > yearNow) {
//                day -= 7
//                dayOfYear -= 7
//            }
//
//        }
//
//        viewState.day = day
//        viewState.dayOfYear = dayOfYear
//        viewState.month = month
//        viewState.year = year
//    }
//
//    fun nextImageButtonClick() {
//
//        if (direction == Entity.YEAR) {
//            val yearNow = Calendar.YEAR
//            if (year > yearNow) {
//                year += 1
//            }
//
//        }
//
//        if (direction == Entity.MONTH) {
//            val monthNow = Calendar.MONTH
//            val yearNow = Calendar.YEAR
//            if (month == 12) {
//                month = 0
//                year += 1
//            }
//            if (month > monthNow || year > yearNow) {
//                month += 1
//            }
//
//        }
//
//        if (direction == Entity.WEEK) {
//            val dayNow = Calendar.DAY_OF_MONTH
//            val monthNow = Calendar.MONTH
//            val yearNow = Calendar.YEAR
//            if (day >= 24) {
//                if (month == 12) {
//                    month = 0
//                    year += 1
//                }
//                month += 1
//                day = -6
//            }
//            if (day > dayNow || month > monthNow || year > yearNow) {
//                day += 7
//                dayOfYear += 7
//            }
//
//        }
//
//        viewState.day = day
//        viewState.dayOfYear = dayOfYear
//        viewState.month = month
//        viewState.year = year
//    }
//
//    fun barChartDataCount() {
//        val dayOfWeek = 2 // Monday
//        val now = Calendar.getInstance()
//        now.set(year, month, day)
//        val weekday = now[Calendar.DAY_OF_WEEK]
//        val days = (dayOfYear - weekday + dayOfWeek) % 7
//        now.add(Calendar.DAY_OF_YEAR, days)
//        val weekStart = now.time
//        now.add(Calendar.DAY_OF_YEAR, 6)
//        val weekEnd = now.time
//        val period: ClosedRange<Date> = (weekStart..weekEnd)
//
//        weekStartGlobal = dateFormatForDay.format(weekStart)
//        weekEndGlobal = dateFormatForDay.format(weekEnd)
//
//        Log.d("now11", now.toString())
//        Log.d("calendarYear", weekEnd.toString())
//
//        structureDateList.clear()
//// GroupingBy year, month, day
//        if (direction == Entity.YEAR) {
//
//            for (month in Calendar.JANUARY..Calendar.DECEMBER) {
//                val list = arrayListOf<User>()
//                allDateBarList.forEach { date ->
//                    if ((date.starredAt.getDateYear()) == year && date.starredAt.getDateMonth() == month) {
//                        list.add(date.user)
//                    }
//                }
//                structureDateList.add(RepoDateStatistic(starredAt =, list))
//            }
//
//        }
//
//        if (direction == Entity.MONTH) {
//
//            // TODO: move to Repository
//            for (week in 0..4) {
//                val list = arrayListOf<User>()
//                allDateBarList.forEach { date ->
//                    if ((date.starredAt.getDateYear()) == year && date.starredAt.getDateMonth() == month &&
//                        (date.starredAt.getDateDayOfMonth() / 7 - 1) == week
//                    ) {
//                        list.add(date.user)
//
//                    }
//                }
//                structureDateList.add(RepoDateStatistic(week, list))
//            }
//
//        }
//
//        if (direction == Entity.WEEK) {
//
//            for (day in 0..6) {
//                val list = arrayListOf<User>()
//                allDateBarList.forEach { date ->
//                    if ((date.starredAt.getDateYear()) == year && date.starredAt.getDateMonth() == month &&
//                        date.starredAt in period && date.starredAt.day == day
//                    ) {
//                        list.add(date.user)
//                    }
//                }
//                structureDateList.add(RepoDateStatistic(day, list))
//            }
//
//        }
//
//        viewState.structureDateList = structureDateList
//        viewState.weekStartGlobal = weekStartGlobal
//        viewState.weekEndGlobal = weekEndGlobal
//    }



}