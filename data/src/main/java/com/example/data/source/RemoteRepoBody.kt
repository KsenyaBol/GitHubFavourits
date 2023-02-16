package com.example.data.source

import com.example.domain.entity.Repo
import com.squareup.moshi.Json

data class RemoteRepoBody(

    @Json(name = "id")
    override var id: Int,

    @Json(name = "name")
    override val name: String,

    @Json (name = "stargazers_count")
    override val stargazers: Int,

    @Json(ignore = true)
    override val favorite: Boolean? = null,

): Repo