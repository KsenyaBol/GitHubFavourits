package com.example.data.entities.api

import com.example.domain.objects.repository.Repositories
import com.example.domain.objects.repository.RepositoriesObject
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponceReposytories (

    @Json(name = "name")
    override var name: String,

    ): Repositories
