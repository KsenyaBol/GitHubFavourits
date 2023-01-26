package com.example.data.entities

import com.example.domain.entity.Favourite
import com.squareup.moshi.Json

data class RemoteFavouriteBody (

    @Json(name = "stargazers_count")
    override val favourite: Int

) : Favourite