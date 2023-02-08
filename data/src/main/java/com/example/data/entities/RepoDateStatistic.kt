package com.example.data.entities

import com.example.domain.entity.DateStatistic
import com.example.domain.entity.User
import java.util.*

data class RepoDateStatistic(

    override val favouriteAt: Date,

    override val userList: List<User>,

    ) : DateStatistic
