package com.santansarah.domain.usecases

import com.santansarah.data.ExposedResult
import com.santansarah.data.UserApp
import com.santansarah.data.UserAppDao
import com.santansarah.utils.UseCaseResult

class DoesAppExist(
    private val userAppDao: UserAppDao
) {
    suspend operator fun invoke(userApp: UserApp): UseCaseResult {

        return when (val dbResult = userAppDao.checkForDupApp(userApp)) {
            is ExposedResult.Success -> UseCaseResult.Success
            is ExposedResult.Error -> UseCaseResult.Failure(dbResult.appErrorCodes!!)
        }
    }

}