package com.santansarah.domain.usecases

import com.santansarah.data.models.User
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserResponse
import com.santansarah.utils.ServiceResult
import com.santansarah.utils.toDatabaseString
import java.time.LocalDateTime

class InsertNewUser
    (
    private val validateUserEmail: ValidateUserEmail,
    private val IUserDao: IUserDao
) {

    suspend operator fun invoke(user: User): UserResponse {

        return when (val result = validateUserEmail(user)) {
            is ServiceResult.Success -> {
                val dbResult = IUserDao.insertUser(user.copy(
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