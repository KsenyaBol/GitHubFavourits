package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repo
import com.example.githubfavourits.ui.base.BasePresenter
import com.example.githubfavourits.ui.statistic.StatisticActivity
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener

class SearchPresenter : BasePresenter<SearchView>() {

    private var nameUser: String = ""
    private var nextQuery: Boolean = false

//    fun onButtonSearchClicked(repos: String, page: Int) {
//        nameUser = repos
//        launchWithWaiting {
//            val repositories = repoRepository.getRepoList(repos, page)
//            viewState.repoList = repositories
//        }
//    }

    fun onButtonSearchClicked(repos: String, recyclerView: OmegaRecyclerView) {
        nameUser = repos
        recyclerView.setPaginationCallback(object : OnPageRequestListener {
            override fun onPageRequest(page: Int) {

                launchWithWaiting {
                    if (!nextQuery) {
                        val repositories = repoRepository.getRepoList(nameUser, page)
                        nextQuery = repositories.allDataLoaded
                        viewState.repoList = repositories
                    }
                }

                if (nextQuery) {
                    recyclerView.hidePagination()
                }
                if (!nextQuery) {
                    recyclerView.showProgressPagination()
                }

            }

            override fun getPagePreventionForEnd(): Int {
                return 5
            }
        })
    }

    fun onReposButtonClicked(repo: Repo) {
        StatisticActivity.createLauncher(nameUser, repo).launch()
    }

}