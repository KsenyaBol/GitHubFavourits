package com.example.data.entities

import com.example.domain.entity.User
import com.squareup.moshi.Json

data class UserData(

    @Json(name = "login")
    override val login: String,

    @Json(name = "avatar_url")
    override val avatarUrl: String,

    ): User
