package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.domain.AppErrors
import com.santansarah.utils.UseCaseResult

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

    operator fun invoke(user: User): UseCaseResult {

     return if (user.email.isBlank() || !user.email.matches(emailAddressRegex))
            UseCaseResult.Failure(AppErrors.invalidEmail)
        else
            UseCaseResult.Success
    }

}