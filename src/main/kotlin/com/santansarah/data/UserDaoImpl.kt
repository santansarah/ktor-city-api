package com.santansarah.data

import com.santansarah.data.DatabaseFactory.dbQuery
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.sqlite.SQLiteErrorCode
import java.sql.SQLIntegrityConstraintViolationException

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

    override suspend fun getUser(user: User): ServiceResult<User> {
        return try {
            val dbUser = dbQuery {
                Users.select {
                    (Users.userId eq user.userId) and (Users.email eq user.email)
                }.map(::resultRowToUser)
                    .single()
            }

            ServiceResult.Success(dbUser)
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                is NoSuchElementException -> ServiceResult.Error(ErrorCode.UNKNOWN_USER)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun insertUser(user: User): ServiceResult<User> {
        return try {
            dbQuery {
                Users
                    .insert {
                        it[email] = user.email
                        it[userCreateDate] = user.userCreateDate
                    }
                    .resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(resultRowToUser(it))
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
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