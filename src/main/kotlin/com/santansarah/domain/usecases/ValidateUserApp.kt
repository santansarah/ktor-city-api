package com.santansarah.domain.usecases

import com.santansarah.data.AppType
import com.santansarah.data.UserApp
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult

class ValidateUserApp() {

    operator fun invoke(userApp: UserApp): ServiceResult<Boolean> {

        return if (userApp.appName.isBlank() || userApp.appType == AppType.NOTSET)
            ServiceResult.Error(ErrorCode.INVALID_APP)
        else
            ServiceResult.Success(true)

    }

}