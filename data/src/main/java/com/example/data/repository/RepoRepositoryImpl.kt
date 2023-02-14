package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.entities.RepoDateStatistic
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.User
import com.example.domain.repository.RepoRepository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*

class RepoRepositoryImpl(override var currentDate: Date) : RepoRepository {

    var allDateList = listOf<DateStatistic>()
    private val allDateBarList = arrayListOf<DateStatistic>()
    private var structureDateList = arrayListOf<DateStatistic>()
    //    private var direction = Entity.YEAR
    private var dateFormatForYear: DateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private var dateFormatForMonth: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
    private var dateFormatForDay: DateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private var year: Int = dateFormatForYear.format(currentDate).toInt()
    private var month = dateFormatForMonth.format(currentDate).toInt()
    private var day = dateFormatForDay.format(currentDate).toInt()
    private var dayOfYear = 0
    private var weekStartGlobal = "day"
    private var weekEndGlobal = "day"

    enum class DateValue() {
        WEEK, MONTH, YEAR
    }

    override suspend fun getRepoList(userName: String): List<Repo> {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStarredList(userName: String, repoName: String): List<DateStatistic> {
        val starredAtList = getStarredList(userName, repoName)
        allDateList = starredAtList

        val adjusters: MutableMap<String, TemporalAdjuster> = HashMap()
        val adj: TemporalAdjuster = TemporalAdjusters.firstDayOfYear()

        adjusters["week"] = TemporalAdjusters.previousOrSame(DayOfWeek.of(1))
        adjusters["month"] = TemporalAdjusters.firstDayOfMonth()
        adjusters["year"] = TemporalAdjusters.firstDayOfYear()
//
//        var temporal = adj.adjustInto()
//        temporal = temporal.with(adj)

        val group = allDateList.groupBy { it.starredAt == adjusters["DateValue.WEEK"]}
        group.values.map {  }


        val list = arrayListOf<User>()
        allDateList.forEach { date ->
            if(dateFormatForYear.format(date.starredAt).toInt() == year) {
                list.addAll(date.userList)
            }
        }

        structureDateList.add(RepoDateStatistic(starredAt = ))
    }
}