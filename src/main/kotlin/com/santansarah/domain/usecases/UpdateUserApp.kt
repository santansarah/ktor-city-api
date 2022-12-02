package com.santansarah.domain.usecases

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult

class UpdateUserApp
    (
    private val validateUserApp: ValidateUserApp,
    private val userAppDao: IUserAppDao
) {

    suspend operator fun invoke(userWithApp: UserWithApp): UserAppResponse {

        // check for blank fields
        val validateAppResult = validateUserApp(userWithApp)
        if (validateAppResult is ServiceResult.Error) {
            return userResponseError(userWithApp, validateAppResult.error)
        }

        // make sure this app + app type is unique
        val checkAppAndType = userAppDao.checkForDupApp(userWithApp)
        if (checkAppAndType is ServiceResult.Error) {
            return userResponseError(userWithApp, checkAppAndType.error)
        }

        // run the update
        val dbResult = userAppDao.updateAppById(userWithApp)
        if (dbResult is ServiceResult.Success && !dbResult.data)
            return userResponseError(userWithApp, ErrorCode.DATABASE_ERROR)
        if (dbResult is ServiceResult.Error)
            return userResponseError(userWithApp, dbResult.error)

        // return the updated app w/the user info
        return when (val updatedApp = userAppDao.getAppById(userWithApp.userAppId)) {
                is ServiceResult.Error -> userResponseError(userWithApp, updatedApp.error)
                is ServiceResult.Success -> UserAppResponse(
                    listOf(updatedApp.data)
                )
        }

    }

    private fun userResponseError(userWithApp: UserWithApp, errorCode: ErrorCode): UserAppResponse {
        return UserAppResponse(listOf(userWithApp), listOf(ResponseErrors(errorCode, errorCode.message)))
    }
}