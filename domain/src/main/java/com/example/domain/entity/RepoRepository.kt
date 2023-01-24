package com.example.domain.entity

interface  RepoRepository {

    val id: Int
    val reposName: String
    val myFavourites: Boolean?

}
