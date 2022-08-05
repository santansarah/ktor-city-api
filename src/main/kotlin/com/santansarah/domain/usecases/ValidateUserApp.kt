package com.santansarah.domain.usecases

import com.santansarah.data.AppType
import com.santansarah.data.UserApp
import com.santansarah.data.UserWithApp
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult

class ValidateUserApp() {

    operator fun invoke(userWithApp: UserWithApp): ServiceResult<Boolean> {

        return if (userWithApp.appName.isBlank() ||
            userWithApp.appType == AppType.NOTSET || userWithApp.email.isNullOrBlank())
            ServiceResult.Error(ErrorCode.INVALID_APP)
        else
            ServiceResult.Success(true)

    }

}