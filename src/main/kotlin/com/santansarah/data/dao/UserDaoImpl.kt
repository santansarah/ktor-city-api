package com.santansarah.data.dao

import com.santansarah.data.*
import com.santansarah.data.DatabaseFactory.dbQuery
import com.santansarah.data.models.User
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.sqlite.SQLiteErrorCode

class UserDaoImpl : IUserDao {

    /**
     * Maps a [ResultRow] to a [User] object.
     */
    private fun resultRowToUser(row: ResultRow) = User(
        userId = row[Users.userId],
        email = row[Users.email],
        name = row[Users.name],
        userCreateDate = row[Users.userCreateDate],
    )

    /**
     * Maps a [UserWithApp] object.
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

    /**
     * Gets a [User] by the email we get from JWT.
     */
    override suspend fun getUserByEmail(email: String): ServiceResult<User> {
        return getUser((Users.email eq email))
    }

    /**
     * Gets a [User] by the id that's stored in our Android app
     * UserPreferences Datastore.
     */
    override suspend fun getUserById(id: Int): ServiceResult<User> {
        return getUser((Users.userId eq id))
    }

    /**
     * Helper function that takes a sql query ([User] by email or id).
     */
    private suspend fun getUser(sql: Op<Boolean>): ServiceResult<User> {
        return try {
            val dbUser = dbQuery {
                Users.select {
                    sql
                }.map(::resultRowToUser)
                    .single()
            }
            ServiceResult.Success(dbUser)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> {
                    ServiceResult.Error(ErrorCode.UNKNOWN_USER)
                }
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }


    /**
     * Gets a [User] by userId and email. This is used to verify a
     * new [UserApp] insert.
     */
    override suspend fun doesUserExist(userId: Int, email: String): ServiceResult<Boolean> {
        return try {
            val isEmpty = dbQuery {
                Users.select {
                    (Users.userId eq userId) and (Users.email eq email)
                }.empty()
            }

            if (isEmpty)
                ServiceResult.Error(ErrorCode.UNKNOWN_USER)
            else
                ServiceResult.Success(true)

        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    /**
     * Inserts a [User]. There is a unique constraint on the email
     * field, so if it's a dup, it will throw an exception.
     * I'm able to get the org.sqlite info in the exception;
     * pretty cool.
     */
    override suspend fun insertUser(user: User): ServiceResult<User> {
        return try {
            dbQuery {
                Users
                    .insert {
                        it[email] = user.email
                        it[name] = user.name
                        it[userCreateDate] = user.userCreateDate
                    }
                    .resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(resultRowToUser(it))
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    if (e.errorCode == SQLiteErrorCode.SQLITE_CONSTRAINT.code)
                        ServiceResult.Error(ErrorCode.EMAIL_EXISTS)
                    else
                        ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }
}