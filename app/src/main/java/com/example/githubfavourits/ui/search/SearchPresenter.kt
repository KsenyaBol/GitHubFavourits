package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter
import com.example.githubfavourits.ui.statistic.StatisticActivity

class SearchPresenter : BasePresenter<SearchView>() {

    private var nameUser: String = ""

    fun onButtonSearchClicked(repos: String) {
        nameUser = repos
        launchWithWaiting {
            val repositories = repoRepository.getRepoList(repos)
            viewState.repoList = repositories
        }
    }

    fun onReposButtonClicked(repo: Repo) {
        StatisticActivity.createLauncher(nameUser, repo).launch()
    }

}