package com.example.data.source

import com.example.data.api.GitHubApi
import com.example.data.repository.DataStarredAtSource
import com.example.domain.entity.StarredRepository
import com.omega_r.base.data.sources.OmegaRemoteSource

class RemoteStarredSource(private val gitHubApi: GitHubApi): OmegaRemoteSource(), DataStarredAtSource {

    override suspend fun getStarredList(userName: String, repoName: String): List<StarredRepository> {
        return gitHubApi.getStarredAtData(userName, repoName)
    }

}