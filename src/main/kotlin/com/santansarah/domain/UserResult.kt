package com.santansarah.domain

import kotlinx.serialization.Serializable

/**
 * Types of errors we can get for a Client.
 */
enum class ErrorCode {
    INVALID_EMAIL,
    EMAIL_EXISTS,
    API_KEY,
    UNKNOWN
}

/**
 * Storage for Client errors.
 */
@Serializable
data class AppErrorCodes(
    val errorId: ErrorCode,
    val errorMessage: String
)

/**
 * Singleton - Client error codes with friendly message.
 */
object UserErrors {
    val invalidEmail = AppErrorCodes(ErrorCode.INVALID_EMAIL, "Invalid email.")
    val userExists = AppErrorCodes(ErrorCode.EMAIL_EXISTS, "This email address is already registered.")
    val databaseError = AppErrorCodes(ErrorCode.UNKNOWN, "Unknown database error. Try again, and check your parameters.")
    //val apiKeyExists = AppErrorCodes(ErrorCode.API_KEY, "There was a problem generating your API Key. Try again.")
}

/**
 * Use case results.
 */
sealed class UserResult {
    object Success : UserResult()
    data class Failure(val error: AppErrorCodes) : UserResult()
}



