package com.santansarah.domain

import com.santansarah.data.User
import com.santansarah.utils.ErrorCode

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class ResponseErrors(val code: ErrorCode, val message: String)
