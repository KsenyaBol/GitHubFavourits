package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter

class SearchPresenter : BasePresenter<SearchView>() {

    fun onButtonSearchClicked(repos: String) {
        launchWithWaiting {
            val repositories = repoRepository.getRepoList(repos)
            viewState.repoList = repositories
        }
    }

    fun onReposButtonClicked(repo: Repo) {
        showComingSoon()
    }

}