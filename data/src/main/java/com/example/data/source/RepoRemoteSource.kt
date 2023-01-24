package com.example.data.source

import com.example.data.api.GitHubApi
import com.example.data.entities.api.ResponceRepositories
import com.omega_r.base.data.sources.OmegaRemoteSource

class RepoRemoteSource(private val gitHubApi: GitHubApi): OmegaRemoteSource() {

    suspend fun getRepositoryInfo(name: String): List<ResponceRepositories> {
        return gitHubApi.getRepositoriesData(name)
    }

}