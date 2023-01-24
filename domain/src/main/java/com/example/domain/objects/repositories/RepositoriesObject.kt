package com.example.domain.objects.repositories

import com.example.domain.entity.Repositories

data class RepositoriesObject (

    override var id: Int,
    override var reposName: String,
    override var myFavourites: Boolean,

    ): Repositories
