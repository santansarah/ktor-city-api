package com.santansarah.data

import com.santansarah.data.DatabaseFactory.dbQuery
import com.santansarah.domain.AppErrors
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.select

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

    override suspend fun checkForDupApp(userApp: UserApp): ExposedResult<Boolean> {
        return try {
            val isEmpty = dbQuery {
                UserApps.select {
                    (UserApps.userId eq userApp.userId) and
                            (UserApps.appName eq userApp.appName) and
                            (UserApps.appType eq userApp.appType)
                }.empty()
            }
            if (isEmpty)
                ExposedResult.Success(false)
            else
                ExposedResult.Error(true, AppErrors.appExists)
        }
        catch (e: ExposedSQLException) {
            ExposedResult.Error(false, AppErrors.databaseError)
        }
    }

    override suspend fun getUserWithApp(apiKey: String): UserWithApp? {
        return dbQuery {
            (Users innerJoin UserApps).select {
                UserApps.apiKey eq apiKey
            }
                .map(::resultRowToUserWithApp)
                .singleOrNull()
        }
    }


    override suspend fun doesApiKeyExist(apiKey: String): Boolean {
        /*var apiKeyExists = false
        dbQuery {
           apiKeyExists = UserApps.slice(intLiteral(1)).select { UserApps.apiKey eq apiKey }.count() > 0
        }
        return apiKeyExists
        */
        /**
         * Exposed has an [exists] fun, but it doesn't return a boolean in
         * the right type of way.
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