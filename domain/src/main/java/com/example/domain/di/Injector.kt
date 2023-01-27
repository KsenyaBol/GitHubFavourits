package com.example.domain.di

import com.example.domain.repository.RepoRepository
import com.example.domain.repository.StarredAtRepository

interface Injector {

    val repoRepository: RepoRepository
    val starredAtRepository: StarredAtRepository

}