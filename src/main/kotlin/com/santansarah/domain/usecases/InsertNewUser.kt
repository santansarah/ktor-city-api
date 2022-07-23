package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.UserResponse
import com.santansarah.domain.UserResult

class InsertNewUser
    (
    private val validateUserEmail: ValidateUserEmail,
    private val userDao: UserDao
) {

    suspend operator fun invoke(user: User): UserResponse {
        var userResponse: UserResponse

        return when (val result = validateUserEmail(user)) {
            is UserResult.Success -> {
                val dbResult = userDao.insertUser(user)
                UserResponse(dbResult.data, emptyList())
            }
            is UserResult.Failure -> {
                UserResponse(user, listOf( result.error))
            }
        }
    }
}