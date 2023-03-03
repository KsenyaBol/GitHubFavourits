package com.example.data.source

import com.example.data.api.GitHubApi
import com.example.data.repository.DataRepoSource
import com.example.domain.entity.Repo
import com.omega_r.base.data.sources.OmegaRemoteSource

class RemoteRepoSource(private val gitHubApi: GitHubApi): OmegaRemoteSource(), DataRepoSource {

    override suspend fun getRepoRepositoryList(userName: String, pageNumber: Int): List<Repo> {
        return gitHubApi.getRepositoriesData(userName, pageNumber)
    }

    override suspend fun getStarredList( userName: String, repoName: String, pageNumber: Int): List<RemoteStarredBody> {
        return gitHubApi.getStarredAtData(userName, repoName, pageNumber)
    }

}