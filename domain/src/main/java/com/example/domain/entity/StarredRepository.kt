package com.example.domain.entity

import java.io.Serializable


interface StarredRepository: Serializable {

    val favouriteAt: String

    val user: User

}