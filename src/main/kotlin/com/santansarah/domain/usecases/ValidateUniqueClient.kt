package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.ClientErrors
import com.santansarah.domain.UserResult

class ValidateUniqueClient(
    private val userDao: UserDao
) {

    suspend operator fun invoke(user: User): UserResult {

        var userResult: UserResult = UserResult.Success

        var dbClient = userDao.doesUserExist(user.email)
        if (dbClient != null)
            userResult = UserResult.Failure(ClientErrors.clientExists)

        return userResult
    }

}