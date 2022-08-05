package com.santansarah.data

import org.jetbrains.exposed.sql.Table


/**
 * Create a [User] table, and add a unique constraint
 * to email. The userId is [autoIncrement] - I'll get
 * the whole record back from my DAO on insert.
 */
object Users : Table() {
    val userId = integer("userId").autoIncrement()
    val email = varchar("email", 255)
        .uniqueIndex()
    val userCreateDate = varchar("userCreateDate", length = 255)

    override val primaryKey = PrimaryKey(userId)
}

/**
 * [userId] is indexed for faster processing, and references the
 * [Users] table. [apiKey] must be unique. It will throw in
 * [UserAppDaoImpl] if it's not.
 */
object UserApps : Table() {
    val userAppId = integer("userAppId").autoIncrement()
    val userId = integer("userId")
        .index()
        .references(Users.userId) // Users is parent.
    val appName = varchar("appName", 255)
    val appType = enumeration<AppType>("appType")
    val apiKey = varchar("apiKey", 255)
        .uniqueIndex()
    val userAppCreateDate = varchar("userAppCreateDate", 255)

    override val primaryKey = PrimaryKey(userAppId)
}