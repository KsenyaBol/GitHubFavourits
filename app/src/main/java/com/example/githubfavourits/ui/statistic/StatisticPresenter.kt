package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.repository.RepoRepository
import com.example.githubfavourits.ui.base.BasePresenter
import com.omega_r.libs.extensions.date.getDateDayOfMonth
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class StatisticPresenter(private val nameUser: String, private val repo: Repo) :
    BasePresenter<StatisticView>() {

    private var structureDateList = mutableListOf<DateStatistic>()
    private var direction = RepoRepository.Period.YEAR
    private var currentDate = Date()
    private var year: Int = currentDate.getDateYear()
    private var month = currentDate.getDateMonth()
    private var day = currentDate.getDateDayOfMonth()
    private var dayOfYear = 0


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun getStarredDataList() {

        Log.d("yearNow", year.toString())
        launch {

            structureDateList.clear()

            val dayOfWeek = Calendar.MONDAY
            val now1 = Calendar.getInstance()
            now1.set(year, month, day)
            val weekday = now1[Calendar.DAY_OF_WEEK]
            val days = (LocalDate.now().dayOfYear - weekday + dayOfWeek) % 7
            now1.add(Calendar.DAY_OF_YEAR, days)
            val weekStart = now1.time
            now1.add(Calendar.DAY_OF_YEAR, 6)
            val weekEnd = now1.time

            viewState.weekStartGlobal = weekStart.toString()
            viewState.weekEndGlobal = weekEnd.toString()

            val nameRepo = repo.name
            val now = Date((year - 1900), month, day)

            val starredAtList = repoRepository.getStatisticList(direction, now, nameUser, nameRepo)
            structureDateList.addAll(starredAtList)

            viewState.structureDateList = structureDateList

            Log.d("StatisticPrStarredAt", starredAtList.toString())

        }

    }

    fun onPeriodClicked(direction: RepoRepository.Period) {
        viewState.direction = direction
        this.direction = direction
    }

    fun onPreviousClicked() {

        if (direction == RepoRepository.Period.YEAR) {
            year -= 1
        }

        if (direction == RepoRepository.Period.MONTH) {
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

        if (direction == RepoRepository.Period.WEEK) {
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

        if (direction == RepoRepository.Period.YEAR) {
            val yearNow = Calendar.YEAR
            if (year >= yearNow) {
                year += 1
            }

        }

        if (direction == RepoRepository.Period.MONTH) {
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

        if (direction == RepoRepository.Period.WEEK) {
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


}