package com.example.githubfavourits.ui.statistic

import android.util.Log
import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter

class StatisticPresenter(private val nameUser: String, private val repo: Repo): BasePresenter<StatisticView>() {

    fun getStarredDataList() {
        launchWithWaiting {
            val nameRepo = repo.name
            val starredAtList = starredAtRepository.getStarredList(nameUser,nameRepo)
            viewState.starredList = starredAtList

            Log.d("starredAtList", starredAtList.toString())
        }


    }


}