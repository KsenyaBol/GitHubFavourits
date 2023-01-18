package com.example.data.entities.api

import com.example.domain.objects.user.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseUser(
    @Json(name = "id")
    override var id: String,

    @Json(name = "repositories")
    override var repos: String,

    @Json(name = "name")
    override var name: String?
) : User