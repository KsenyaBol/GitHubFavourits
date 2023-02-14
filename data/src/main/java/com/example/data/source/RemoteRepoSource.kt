package com.example.data.source

import com.example.data.api.GitHubApi
import com.example.data.repository.DataRepoSource
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.omega_r.base.data.sources.OmegaRemoteSource

class RemoteRepoSource(private val gitHubApi: GitHubApi): OmegaRemoteSource(), DataRepoSource {

    override suspend fun getRepoList(userName: String): List<Repo> {
        return gitHubApi.getRepositoriesData(userName)
    }

    override suspend fun getStarredList(userName: String, repoName: String): List<DateStatistic> {
        return gitHubApi.getStarredAtData(userName, repoName)
    }

}