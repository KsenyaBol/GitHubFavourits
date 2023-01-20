package com.example.data.entities.api

import com.example.domain.objects.repository.Repositories
import com.example.domain.objects.user.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseUser(

    @Json(name = "id")
    override var id: Int,

    @Json(name = "name")
    override var name: String?,

    @Json(name = "full_name")
    override var fullName: String,

    ) : User