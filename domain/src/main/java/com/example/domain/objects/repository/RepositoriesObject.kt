package com.example.domain.objects.repository

data class RepositoriesObject (

    override var id: Int,
    override var name: String,
    override var start: String,
    override var favouriteCount: Int,
    override var fullName: String,

    ): Repositories
