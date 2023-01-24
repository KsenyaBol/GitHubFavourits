package com.example.data.entities.api

import com.example.domain.entity.Repositories
import com.squareup.moshi.Json

data class ResponceRepositories(

    @Json(name = "id")
    override var id: Int,

    @Json(name = "name")
    override val reposName: String,

    override val myFavourites: Boolean?,

    ): Repositories