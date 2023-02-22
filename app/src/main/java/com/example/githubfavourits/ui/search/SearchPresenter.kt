package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter
import com.example.githubfavourits.ui.statistic.StatisticActivity

class SearchPresenter : BasePresenter<SearchView>() {

    private var nameUser: String = ""
    private var nextQuery: Boolean = false

    fun onReposButtonClicked(repo: Repo) {
        StatisticActivity.createLauncher(nameUser, repo).launch()
    }
    fun requestRepoList(repos: String, page: Int) {
        nameUser = repos
        launchWithWaiting {
            if (!nextQuery){
                val repositories = repoRepository.getRepoList(repos, page)
                nextQuery = repositories.allDataLoaded
                viewState.repoList = repositories
            }
        }

    }

}