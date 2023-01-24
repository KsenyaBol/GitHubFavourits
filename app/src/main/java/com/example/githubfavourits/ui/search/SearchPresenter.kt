package com.example.githubfavourits.ui.search

import com.example.githubfavourits.ui.base.BasePresenter

class SearchPresenter: BasePresenter<SearchView>() {

    fun onButtonSearchClicked(repos: String) {
        launchWithWaiting {
            val repositories = repoRepository.getRepositories(repos)
            viewState.setRepository(repositories)
        }

    }


}