package com.santansarah.utils

/**
 * Here's a list of my app error codes, which include the types of errors
 * that routes can produce. In my Android app, I can easily show these errors
 * in a snackbar.
 */
enum class ErrorCode(val message: String) {
    INVALID_USER("Invalid user object. Check your JSON values."),
    INVALID_EMAIL("Invalid email."),
    EMAIL_EXISTS("This email address is already registered."),
    API_KEY("There was a problem generating your API Key. Try again."),
    UNKNOWN("An unknown error has occurred."),
    UNKNOWN_USER("This user doesn't exist."),
    UNKNOWN_APP("This app doesn't exist."),
    UNKNOWN_CITY("This city doesn't exist."),
    APP_EXISTS("This app already exists."),
    INVALID_APP("UserId, email, app name, and app type can not be blank. Use 'dev' or 'prod' for app type."),
    DATABASE_ERROR("Unknown database error. Try again, and check your parameters."),
    INVALID_JSON("Your JSON must match the format in this sample response."),
    INVALID_CITY_QUERY("You must pass a city name or zip prefix."),
    INVALID_API_KEY("Bad API key. Use x-api-key in the header."),
    INVALID_GOOGLE_CREDENTIALS("Invalid Google sign in.")
}

class AuthenticationException : RuntimeException()
//class GoogleException(val realm: String) : RuntimeException()
class GoogleException() : RuntimeException()