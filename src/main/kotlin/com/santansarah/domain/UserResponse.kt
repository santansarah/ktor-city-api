package com.santansarah.domain

import com.santansarah.data.User
import com.santansarah.data.UserApp
import com.santansarah.data.UserWithApp
import com.santansarah.utils.ErrorCode

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class UserAppResponse(
    val userWithApp: UserWithApp,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class ResponseErrors(val code: ErrorCode, val message: String)
