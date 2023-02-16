package com.example.domain.entity

import java.io.Serializable

interface Repo: Serializable {

    val id: Int

    val name: String

    val stargazers: Int

    val favorite: Boolean?

}