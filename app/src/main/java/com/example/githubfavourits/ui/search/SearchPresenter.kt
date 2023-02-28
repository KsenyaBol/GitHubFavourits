package com.example.githubfavourits.ui.search

import android.util.Log
import com.example.domain.entity.Repo
import com.example.domain.entity.RepoData
import com.example.githubfavourits.ui.base.BasePresenter
import com.example.githubfavourits.ui.statistic.StatisticActivity
import kotlinx.coroutines.launch

class SearchPresenter : BasePresenter<SearchView>() {

    private var nameUser: String = ""
    private var repoList = arrayListOf<Repo>()

    fun onReposButtonClicked(repo: Repo) {
        StatisticActivity.createLauncher(nameUser, repo).launch()
    }
    fun requestRepoList(repos: String, page: Int) {
        nameUser = repos
        launch {
//            if (viewState.nextQuery == 100){

                val repositories = repoRepository.getRepoList(repos, page)
                repositories.repoList.forEach { repos ->
                    repoList.add(repos)
                }

                viewState.nextQuery = repositories.allDataLoaded
                viewState.repoList = RepoData(repoList, repositories.allDataLoaded)
//            }
        }

        Log.d("pageNumber", page.toString())
    }

}