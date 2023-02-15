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
    private var direction = DateValue.YEAR
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
            val starredAtList = repoRepository.getStatisticList(nameUser, nameRepo)
            allDateBarList.addAll(starredAtList)

            viewState.allDateBarList = allDateBarList

            Log.d("allDateBarList", allDateBarList.toString())
            Log.d("starredAtList", starredAtList.toString())

        }

    }

    fun yearButtonClicked() {
        direction = DateValue.YEAR
        viewState.direction = direction
    }

    fun monthButtonClicked() {
        direction = DateValue.MONTH
        viewState.direction = direction
    }

    fun weekButtonClicked() {
        direction = DateValue.WEEK
        viewState.direction = direction
    }

    fun backImageButtonClick() {

        if (direction == DateValue.YEAR) {
            year -= 1
        }

        if (direction == DateValue.MONTH) {
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

        if (direction == DateValue.WEEK) {
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
            if (day > dayNow || month > monthNow || year > yearNow) {
                day -= 7
                dayOfYear -= 7
            }

        }

        viewState.day = day
        viewState.dayOfYear = dayOfYear
        viewState.month = month
        viewState.year = year
    }

    fun nextImageButtonClick() {

        if (direction == DateValue.YEAR) {
            val yearNow = Calendar.YEAR
            if (year > yearNow) {
                year += 1
            }

        }

        if (direction == DateValue.MONTH) {
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

        if (direction == DateValue.WEEK) {
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

    fun barChartDataCount() {
        val dayOfWeek = 2 // Monday
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

        if (direction == DateValue.YEAR) {

            for (date in allDateBarList.indices) {
                for (month in 0..12) {
                    val list = arrayListOf<User>()
                    if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() == month) {
                        list.addAll(allDateBarList[date].userList)
                    }
                    structureDateList.add(RepoDateStatistic(allDateBarList[date].starredAt, list))
                }
            }

        }

        if (direction == DateValue.MONTH) {

            for (date in allDateBarList.indices) {
                for (week in 0..4) {
                    val list = arrayListOf<User>()
                        if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() == month &&
                            (allDateBarList[date].starredAt.getDateDayOfMonth() / 7 - 1) == week) {
                            list.addAll(allDateBarList[date].userList)
                        }
                    structureDateList.add(RepoDateStatistic(allDateBarList[date].starredAt, list))
                }
            }

        }

        if (direction == DateValue.WEEK) {

            for (date in allDateBarList.indices) {
                for (day in 0..6) {
                    val list = arrayListOf<User>()
                        if ((allDateBarList[date].starredAt.getDateYear()) == year && allDateBarList[date].starredAt.getDateMonth() == month &&
                            allDateBarList[date].starredAt in period && allDateBarList[date].starredAt.day == day
                        ) {
                            list.addAll(allDateBarList[date].userList)
                        }

                    structureDateList.add(RepoDateStatistic(allDateBarList[date].starredAt, list))
                }
            }


        }

        viewState.structureDateList = structureDateList
        viewState.weekStartGlobal = weekStartGlobal
        viewState.weekEndGlobal = weekEndGlobal
    }


    enum class DateValue() {
        WEEK, MONTH, YEAR
    }


}