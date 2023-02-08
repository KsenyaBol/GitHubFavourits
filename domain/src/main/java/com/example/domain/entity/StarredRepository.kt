package com.example.domain.entity

import java.io.Serializable

//TODO del
interface StarredRepository: Serializable {

    val starredAt: String

    val user: User

}