package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.utils.UseCaseResult

class DoesAppExist(
    private val userDao: UserDao
) {

    suspend operator fun invoke(user: User): UseCaseResult {

        var userResult: UseCaseResult = UseCaseResult.Success

/*
        var dbClient = userDao.doesUserExist(user.email)
        if (dbClient != null)
            userResult = UserResult.Failure(UserErrors.clientExists)
*/

        return userResult
    }

}