package com.example.data.source

import com.example.domain.entity.StarredRepository
import com.squareup.moshi.Json

data class RemoteStarredBody (

    @Json(name = "starred_at")
    override val starredAt: String,

    @Json(name = "user")
    override val user: RemoteUserBody

) : StarredRepository