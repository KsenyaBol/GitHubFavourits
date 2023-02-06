package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.util.Log
import com.example.data.entities.StarredData
import com.example.domain.entity.Repo
import com.example.domain.entity.StarredRepoData
import com.example.githubfavourits.ui.base.BasePresenter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class StatisticPresenter(private val nameUser: String, private val repo: Repo) : BasePresenter<StatisticView>() {

    @SuppressLint("SimpleDateFormat")
    fun getStarredDataList() {
        launchWithWaiting {
            val allDateBarList = arrayListOf<StarredRepoData>()
            val nameRepo = repo.name
            val starredAtList = starredAtRepository.getStarredList(nameUser, nameRepo)

            starredAtList.forEach { starred ->

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                try {
                    val date: Date = format.parse(starred.favouriteAt)!!
                    allDateBarList.add(StarredData(date, starred.user))
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }
            viewState.allDateBarList = allDateBarList

            Log.d("allDateBarList", allDateBarList.toString())
            Log.d("starredAtList", starredAtList.toString())

        }


    }


}