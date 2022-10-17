package com.santansarah.domain.usecases

import com.santansarah.data.models.User
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult

class ValidateUserEmail() {

    companion object {
        private val EMAIL_REGEX = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }

    operator fun invoke(user: User): ServiceResult<Boolean> {

     return if (user.email.isBlank() || !user.email.matches(EMAIL_REGEX))
         ServiceResult.Error(ErrorCode.INVALID_EMAIL)
        else
         ServiceResult.Success(true)
    }

}