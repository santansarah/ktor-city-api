package com.santansarah.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int = 0,
    val email: String = "",
    val userCreateDate: String = "",
    val apps: List<UserApp> = emptyList()
)

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
    DEVELOPMENT(1),
    PRODUCTION(2)
}
