package com.santansarah.utils

/**
 * Types of errors that routes could produce. These
 * codes are returned in the response body.
 */
enum class ErrorCode(val message: String) {
    INVALID_USER("Invalid user object. Check your JSON values."),
    INVALID_EMAIL("Invalid email."),
    EMAIL_EXISTS("This email address is already registered."),
    API_KEY("There was a problem generating your API Key. Try again."),
    UNKNOWN("An unknown error has occurred."),
    UNKNOWN_USER("This user doesn't exist."),
    UNKNOWN_APP("This app doesn't exist."),
    APP_EXISTS("This app already exists."),
    INVALID_APP("UserId, email, app name, and app type can not be blank. Use 'dev' or 'prod' for app type."),
    DATABASE_ERROR("Unknown database error. Try again, and check your parameters."),
    INVALID_JSON("Your JSON must match the format in this sample response.")
}