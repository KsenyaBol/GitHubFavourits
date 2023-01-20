package com.example.domain.objects.user

import com.example.domain.objects.repository.Repositories
import java.io.Serializable

interface User: Serializable {

    var id: Int
    var name: String?
    var fullName: String

}