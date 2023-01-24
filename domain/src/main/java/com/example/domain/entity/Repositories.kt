package com.example.domain.entity

import java.io.Serializable

interface Repositories: Serializable {

    val id: Int
    val reposName: String
    val myFavourites: Boolean?

}