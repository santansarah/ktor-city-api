package com.santansarah.data.dao

import com.santansarah.data.*
import com.santansarah.data.DatabaseFactory.dbQuery
import com.santansarah.data.models.UserApp
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.sqlite.SQLiteErrorCode

class UserAppDaoImpl : IUserAppDao {

    /**
     * Map my table join query to a [UserWithApp] object
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
     * Map my table to a [UserApp] object.
     */
    private fun resultRowToUserApp(row: ResultRow) = UserApp(
        userId = row[UserApps.userId],
        userAppId = row[UserApps.userAppId],
        appName = row[UserApps.appName],
        appType = row[UserApps.appType],
        apiKey = row[UserApps.apiKey],
        appCreateDate = row[UserApps.userAppCreateDate]
    )

    override suspend fun checkForDupApp(userWithApp: UserWithApp): ServiceResult<Boolean> {
        return try {
            val isEmpty = dbQuery {
                UserApps.select {
                    (UserApps.userId eq userWithApp.userId) and
                            (UserApps.appName eq userWithApp.appName) and
                            (UserApps.appType eq userWithApp.appType)
                }.empty()
            }
            if (isEmpty)
                ServiceResult.Success(false)
            else
                ServiceResult.Error(ErrorCode.APP_EXISTS)
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getUserWithApp(apiKey: String): ServiceResult<UserWithApp> {
        return try {
            val userWithApp = dbQuery {
                (Users innerJoin UserApps).select {
                    UserApps.apiKey eq apiKey
                }
                    .map(::resultRowToUserWithApp)
                    .single()
            }
            ServiceResult.Success(userWithApp)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> ServiceResult.Error(ErrorCode.UNKNOWN_APP)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun insertUserApp(userWithApp: UserWithApp): ServiceResult<UserApp> {
        return try {
            dbQuery {
                UserApps
                    .insert {
                        it[userId] = userWithApp.userId
                        it[appName] = userWithApp.appName
                        it[appType] = userWithApp.appType
                        it[apiKey] = userWithApp.apiKey
                        it[userAppCreateDate] = userWithApp.appCreateDate
                    }
                    .resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(resultRowToUserApp(it))
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    if (e.errorCode == SQLiteErrorCode.SQLITE_CONSTRAINT.code)
                        ServiceResult.Error(ErrorCode.API_KEY)
                    else
                        ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getUserAppsByUserId(userId: Int): ServiceResult<List<UserWithApp>> {
        return try {
            val userAppList = dbQuery {
                (Users innerJoin UserApps).select {
                    UserApps.userId eq userId
                }
                    .map(::resultRowToUserWithApp)
            }
            ServiceResult.Success(userAppList)
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getAppById(appId: Int): ServiceResult<UserWithApp> {
        return try {
            val userWithApp = dbQuery {
                (Users innerJoin UserApps).select {
                    UserApps.userAppId eq appId
                }
                    .map(::resultRowToUserWithApp)
                    .single()
            }
            ServiceResult.Success(userWithApp)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> ServiceResult.Error(ErrorCode.UNKNOWN_APP)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun updateAppById(userWithApp: UserWithApp): ServiceResult<Boolean> {
        return try {
            val updateResult = dbQuery {
                UserApps.update(
                    { UserApps.userAppId eq userWithApp.userAppId }
                )
                {
                    it[appName] = userWithApp.appName
                    it[appType] = userWithApp.appType
                }
            }
            // 0 = false, 1 = success/true
            ServiceResult.Success(updateResult == 1)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> ServiceResult.Error(ErrorCode.UNKNOWN_APP)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }
}