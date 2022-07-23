package com.santansarah.data

import com.santansarah.data.DatabaseFactory.dbQuery
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import sun.rmi.runtime.Log

class UserDaoImpl : UserDao {

    /**
     * map our table to a [User] object
     */
    private fun resultRowToUser(row: ResultRow) = User(
        userId = row[Users.userId],
        email = row[Users.email],
        userCreateDate = row[Users.userCreateDate],
    )

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

    override suspend fun doesUserExist(email: String): User? {
        return dbQuery {
            Users.select {
                (Users.email eq email)
            }
                .map(::resultRowToUser)
                .singleOrNull()
        }
    }

    override suspend fun insertUser(user: User): ExposedResult<User> {
        return try {
            dbQuery {
                Users
                    .insert {
                        it[email] = user.email
                        it[userCreateDate] = user.userCreateDate
                    }
                    .resultedValues?.singleOrNull()?.let {
                        ExposedResult.Success(resultRowToUser(it))
                    } ?: ExposedResult.Error(user, "Error inserting new user. Try again.")
            }
        } catch (e: ExposedSQLException) {
            println(e)
            ExposedResult.Error(user, "Database error.")
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

}