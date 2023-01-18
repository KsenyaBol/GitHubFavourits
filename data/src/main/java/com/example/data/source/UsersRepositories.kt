package com.example.data.source

import com.example.data.api.GitHubApi
import com.example.data.entities.api.ResponseUser
import com.omega_r.base.data.sources.OmegaRemoteSource

class UsersRepositories(private val gitHubApi: GitHubApi): OmegaRemoteSource() {

    companion object {
        private const val REQUEST_FIELDS = "id,repos,name"
    }

    override suspend fun getUserInfo(name: String): ResponseUser {
        return gitHubApi.getUserData(name, REQUEST_FIELDS)
    }

}