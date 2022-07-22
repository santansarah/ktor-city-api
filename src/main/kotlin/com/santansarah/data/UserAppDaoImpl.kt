package com.santansarah.data

import com.santansarah.data.DatabaseFactory.dbQuery
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserAppDaoImpl : UserAppDao {

    /**
     * map our table join to a [UserWithApp] object
     */
    private fun resultRowToUserWithApp(row: ResultRow) = UserWithApp(
        userId = row[Users.userId],
        email = row[Users.email],
        userCreateDate = row[Users.userCreateDate],
        userAppId = row[UserApps.userAppId],
        appName = row[UserApps.appName],
        appType = row[UserApps.appType],
        apiKey = row[UserApps.apiKey],
        appCreateDate = row[UserApps.userAppCreateDate]
    )

    override suspend fun doesApiKeyExist(apiKey: String): Boolean {
        /*var apiKeyExists = false
        dbQuery {
           apiKeyExists = UserApps.slice(intLiteral(1)).select { UserApps.apiKey eq apiKey }.count() > 0
        }
        return apiKeyExists
        */
        /**
         * Exposed has an [exists] fun, but it doesn't return a boolean!
         * There are a few ways to do this; I like this one.
         */
       return dbQuery {
            UserApps.select { UserApps.apiKey eq apiKey }.empty()
        }
    }

    override suspend fun insertUserApp(userApp: UserApp): UserApp? {
        TODO("Not yet implemented")
    }

}