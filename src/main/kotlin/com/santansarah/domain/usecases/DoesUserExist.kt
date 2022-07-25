package com.santansarah.domain.usecases

import com.santansarah.data.ExposedResult
import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.UserResponse

class DoesUserExist(
    private val userDao: UserDao
) {
    suspend operator fun invoke(user: User): UserResponse {

        return when (val dbResult = userDao.getUser(user)) {
            is ExposedResult.Success -> UserResponse(dbResult.data)
            is ExposedResult.Error -> UserResponse(dbResult.data, listOf(dbResult.appErrorCodes!!))
        }
    }
}