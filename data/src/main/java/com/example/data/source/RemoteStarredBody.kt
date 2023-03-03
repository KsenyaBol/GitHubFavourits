package com.example.data.source

import com.squareup.moshi.Json
import java.util.*

data class RemoteStarredBody (

    @Json(name = "starred_at")
    val starredAt: Date,

    @Json(name = "user")
    val userList: RemoteUserBody

)