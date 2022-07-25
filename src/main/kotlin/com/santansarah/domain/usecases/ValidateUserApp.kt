package com.santansarah.domain.usecases

import com.santansarah.data.AppType
import com.santansarah.data.UserApp
import com.santansarah.domain.AppErrors
import com.santansarah.utils.UseCaseResult

class ValidateUserApp() {

    operator fun invoke(userApp: UserApp): UseCaseResult {

        return if (userApp.appName.isBlank() || userApp.appType == AppType.NOTSET)
            UseCaseResult.Failure(AppErrors.invalidApp)
        else
            UseCaseResult.Success

    }

}