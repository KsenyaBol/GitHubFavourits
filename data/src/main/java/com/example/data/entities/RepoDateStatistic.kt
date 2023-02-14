package com.example.data.entities

import com.example.domain.entity.DateStatistic
import com.example.domain.entity.User
import java.util.*

data class RepoDateStatistic(

    override val starredAt: Date,

    override val userList: List<User>,

    ) : DateStatistic
