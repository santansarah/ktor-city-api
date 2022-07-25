package com.santansarah.utils

import kotlinx.serialization.Serializable

/**
 * Types of errors we can get for a Client.
 */
enum class ErrorCode {
    INVALID_EMAIL,
    EMAIL_EXISTS,
    API_KEY,
    UNKNOWN,
    UNKNOWN_USER,
    APP_EXISTS,
    INVALID_APP
}

/**
 * Storage for App errors.
 */
@Serializable
data class AppErrorCodes(
    val errorId: ErrorCode,
    val errorMessage: String
)

