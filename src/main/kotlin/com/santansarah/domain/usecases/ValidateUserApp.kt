package com.santansarah.domain.usecases

import com.santansarah.data.models.AppType
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult

class ValidateUserApp() {

    operator fun invoke(userWithApp: UserWithApp): ServiceResult<Boolean> {

        return if (userWithApp.appName.isBlank() || userWithApp.appType == AppType.NOTSET)
            ServiceResult.Error(ErrorCode.INVALID_APP)
        else
            ServiceResult.Success(true)

    }

}