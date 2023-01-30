package com.example.domain.entity



interface StarredRepository {
    val favouriteAt: String
    val user: User
}