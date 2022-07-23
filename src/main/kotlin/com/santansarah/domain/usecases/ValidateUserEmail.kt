package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.ClientErrors
import com.santansarah.domain.UserResult

class ValidateUserEmail() {

    private val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    operator fun invoke(user: User): UserResult {

        var userResult: UserResult = UserResult.Success

        if (user.email.isBlank() || !user.email.matches(emailAddressRegex))
            userResult = UserResult.Failure(ClientErrors.invalidEmail)

        return userResult
    }

}