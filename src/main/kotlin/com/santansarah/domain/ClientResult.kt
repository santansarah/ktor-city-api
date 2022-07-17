package com.santansarah.domain

import kotlinx.serialization.Serializable

/**
 * Types of errors we can get for a Client.
 */
enum class ErrorCode(val errId: Int) {
    INVALID_EMAIL(900),
    CLIENT_EXISTS(901),
    API_KEY(902)
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
object ClientErrors {
    val invalidEmail = AppErrorCodes(ErrorCode.INVALID_EMAIL, "Invalid email.")
    val clientExists = AppErrorCodes(ErrorCode.CLIENT_EXISTS, "Client exists. Email, App Name, and App Type must be unique.")
    val apiKeyExists = AppErrorCodes(ErrorCode.API_KEY, "There was a problem generating your API Key. Try again.")
}

/**
 * Use case results.
 */
sealed class ClientResult {
    object Success : ClientResult()
    data class Failure(val error: AppErrorCodes) : ClientResult()
}



