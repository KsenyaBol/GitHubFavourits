package com.example.domain.objects.repositories

import com.example.domain.entity.RepoRepository

data class DataRepoRepository(

    override val id: Int,
    override val reposName: String,
    override val myFavourites: Boolean?,

    ): RepoRepository
