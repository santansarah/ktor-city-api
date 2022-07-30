package com.santansarah.utils

import kotlinx.serialization.Serializable

/**
 * Types of errors we can get for a Client.
 */
enum class ErrorCode(val message: String) {
    INVALID_EMAIL("Invalid email."),
    EMAIL_EXISTS("This email address is already registered."),
    API_KEY("There was a problem generating your API Key. Try again."),
    UNKNOWN("An unknown error has occurred."),
    UNKNOWN_USER("This user doesn't exist."),
    APP_EXISTS("This app already exists."),
    INVALID_APP("App name and app type can not be blank."),
    DATABASE_ERROR("Unknown database error. Try again, and check your parameters.")
}