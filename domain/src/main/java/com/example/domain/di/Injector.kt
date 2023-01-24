package com.example.domain.di

import com.example.domain.entity.RepoRepository

interface Injector {
    val repoRepository: RepoRepository
}