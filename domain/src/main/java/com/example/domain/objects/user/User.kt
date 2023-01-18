package com.example.domain.objects.user

import java.io.Serializable

interface User: Serializable {

    var id: String
    var repos: String
    var name: String?

}