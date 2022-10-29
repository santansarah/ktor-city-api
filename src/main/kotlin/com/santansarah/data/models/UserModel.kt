package com.santansarah.data.models

import com.auth0.jwt.interfaces.Payload
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Holds basic user info that we get from Google
 * JWT.
 */
@Serializable
data class User(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val apps: List<UserApp> = emptyList()
)

/**
 * Extension function that maps a
 * Google JWT [Payload] to a [User].
 */
fun Payload.toUser() =
    try {
        User(
            email = this.getClaim("email").asString(),
            name = this.getClaim("name").asString()
        )
    } catch (e: Exception) {
        println("payload parse error: ${e.message}")
        User()
    }

@Serializable
data class UserApp(
    val userAppId: Int = 0,
    val userId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.NOTSET,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
data class UserWithApp(
    val userId: Int = 0,
    val email: String = "",
    val userCreateDate: String = "",
    val userAppId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.NOTSET,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
enum class AppType(val value: Int) {
    NOTSET(0),

    @SerialName("dev")
    DEVELOPMENT(1),

    @SerialName("prod")
    PRODUCTION(2)
}