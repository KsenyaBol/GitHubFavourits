package com.example.data.entities

import com.example.domain.entity.StarredRepository
import com.squareup.moshi.Json

data class RemoteStarredBody (

    @Json(name = "starred_at")
    override val favouriteAt: String

) : StarredRepository