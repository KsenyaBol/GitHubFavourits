package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter

class StatisticPresenter(private val nameUser: String, private val repo: Repo): BasePresenter<StatisticView>() {

//    init {
//        viewState.nameUser = nameUser
//    }

    fun getStarredDataList() {
        launchWithWaiting {
            val nameRepo = repo.name
            val starredAtList = starredAtRepository.getStarredList(nameUser,nameRepo)
            viewState.starredList = starredAtList
        }


    }




}