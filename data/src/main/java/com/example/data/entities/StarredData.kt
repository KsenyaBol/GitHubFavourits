package com.example.data.entities

import com.example.domain.entity.StarredRepoData
import com.example.domain.entity.User
import java.util.*

data class StarredData(

    override val favouriteAt: Date,

    override val user: User,

    ): StarredRepoData
