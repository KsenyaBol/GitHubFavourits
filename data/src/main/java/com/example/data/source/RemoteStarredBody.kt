package com.example.data.source

import com.example.domain.entity.DateStatistic
import com.example.domain.entity.User
import com.squareup.moshi.Json
import java.util.*

data class RemoteStarredBody (

    @Json(name = "starred_at")
    override val starredAt: Date,

    @Json(name = "user")
    override val userList: List<User>

) : DateStatistic