package com.example.data.entities.api

import com.example.domain.entity.RepositoryFavourite
import com.squareup.moshi.Json

data class ResponceReposytoryFavourite (

    @Json(name = "id")
    override var id: Int,

    @Json(name = "name")
    override var name: String,

    @Json(name = "full_name")
    override var fullName: String,

    @Json(name = "create_at")
    override var start: String,

    @Json(name = "stargazers_count")
    override var favouriteCount: Int,

    ): RepositoryFavourite
