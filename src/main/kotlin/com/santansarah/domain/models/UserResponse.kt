package com.santansarah.domain.models

import com.santansarah.data.models.User
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.ErrorCode

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class UserAppResponse(
    val apps: List<UserWithApp>,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class ResponseErrors(val code: ErrorCode, val message: String)
