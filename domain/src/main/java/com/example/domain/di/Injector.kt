package com.example.domain.di

import com.example.domain.repository.RepoRepository

interface Injector {

    val repoRepository: RepoRepository

}