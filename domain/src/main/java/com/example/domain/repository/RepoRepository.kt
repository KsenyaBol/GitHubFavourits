package com.example.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

interface RepoRepository {

    // TODO: rename getDataStatistics, add group enum, current shift

    var currentDate: Date

    suspend fun getRepoList(userName: String): List<Repo>

    suspend fun getStarredList(userName: String, repoName: String): List<DateStatistic>
}