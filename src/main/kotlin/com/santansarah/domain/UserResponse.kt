package com.santansarah.domain

import com.santansarah.data.User
import com.santansarah.utils.AppErrorCodes

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<AppErrorCodes> = emptyList()
)
