package com.example.domain.repository

import com.example.domain.entity.Repo

interface RepoRepository {

    suspend fun getRepoList(userName: String): List<Repo>

}