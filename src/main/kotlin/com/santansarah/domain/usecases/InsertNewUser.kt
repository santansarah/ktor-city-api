package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.ResponseErrors
import com.santansarah.domain.UserResponse
import com.santansarah.utils.ServiceResult
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
            is ServiceResult.Success -> {
                val dbResult = userDao.insertUser(user.copy(
                    userCreateDate = LocalDateTime.now().toDatabaseString())
                )
                when (dbResult) {
                    is ServiceResult.Success -> UserResponse(dbResult.data, emptyList())
                    is ServiceResult.Error -> UserResponse(user, listOf(ResponseErrors(dbResult.error, dbResult.error.message)))
                }

            }
            is ServiceResult.Error -> UserResponse(user, listOf(ResponseErrors(result.error, result.error.message)))
        }
    }
}