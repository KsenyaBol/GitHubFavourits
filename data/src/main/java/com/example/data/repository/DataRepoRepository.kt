package com.example.data.repository

import com.example.data.source.RemoteStarredBody
import com.example.domain.entity.DateStatistic
import com.example.domain.repository.RepoRepository
import com.omega_r.base.annotations.AppOmegaRepository

@AppOmegaRepository
interface DataRepoRepository: RepoRepository {

    suspend fun getStarredList(pageNumber: Int, userName: String, repoName: String): List<RemoteStarredBody>

}