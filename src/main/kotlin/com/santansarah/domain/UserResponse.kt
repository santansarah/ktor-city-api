package com.santansarah.domain

import com.santansarah.data.User

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<AppErrorCodes> = emptyList()
)
