package com.santansarah.domain.usecases

import com.santansarah.data.ExposedResult
import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.AppErrors
import com.santansarah.domain.UserResponse
import com.santansarah.utils.UseCaseResult
import com.santansarah.utils.toDatabaseString
import java.time.LocalDateTime

class InsertNewUser
    (
    private val validateUserEmail: ValidateUserEmail,
    private val userDao: UserDao
) {

    suspend operator fun invoke(user: User): UserResponse {
        var userResponse: UserResponse

        return when (val result = validateUserEmail(user)) {
            is UseCaseResult.Success -> {
                val dbResult = userDao.insertUser(user.copy(
                    userCreateDate = LocalDateTime.now().toDatabaseString())
                )
                when (dbResult) {
                    is ExposedResult.Success -> UserResponse(dbResult.data, emptyList())
                    is ExposedResult.Error -> UserResponse(user,
                    dbResult.appErrorCodes?.let {
                        listOf(it)
                    } ?: listOf(AppErrors.databaseError))
                }

            }
            is UseCaseResult.Failure -> {
                UserResponse(user, listOf( result.error))
            }
        }
    }
}