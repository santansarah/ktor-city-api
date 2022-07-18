package com.santansarah.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Client(
    val id: Int,
    val email: String = "",
    val appName: String = "",
    val appType: AppType,
    val apiKey: String = ""
)

@Serializable
enum class AppType(val value: Int) {
    DEVELOPMENT(1),
    PRODUCTION(2)
}

object Clients : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255)
    val appName = varchar("appName", 255)
    val appType = enumeration<AppType>("appType")
    val apiKey = varchar("apiKey", 255)

    override val primaryKey = PrimaryKey(id)
}