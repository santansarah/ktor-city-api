package com.santansarah.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

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

object Users : Table() {
    val userId = integer("userId").autoIncrement()
    val email = varchar("email", 255)
    val userCreateDate = varchar("userCreateDate", length = 255)

    override val primaryKey = PrimaryKey(userId)
}

object UserApps : Table() {
    val userAppId = integer("userAppId").autoIncrement()
    val userId = integer("userId")
        .uniqueIndex()
        .references(Users.userId)
    val appName = varchar("appName", 255)
    val appType = enumeration<AppType>("appType")
    val apiKey = varchar("apiKey", 255)
    val userAppCreateDate = varchar("userAppCreateDate", 255)

    override val primaryKey = PrimaryKey(userAppId)
}