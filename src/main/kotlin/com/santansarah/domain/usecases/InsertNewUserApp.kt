package com.santansarah.domain.usecases

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import com.santansarah.utils.toDatabaseString
import java.time.LocalDateTime

class InsertNewUserApp
    (
    private val validateUserApp: ValidateUserApp,
    private val generateApiKey: GenerateApiKey,
    private val IUserAppDao: IUserAppDao,
    private val IUserDao: IUserDao
) {

    suspend operator fun invoke(userWithApp: UserWithApp): UserAppResponse {

        // check for blank fields
        val validateAppResult = validateUserApp(userWithApp)
        if (validateAppResult is ServiceResult.Error) {
            return userResponseError(userWithApp, validateAppResult.error)
        }

        // make sure the user exists
        val checkUser = IUserDao.doesUserExist(userWithApp.userId, userWithApp.email)
        if (checkUser is ServiceResult.Error)
            return userResponseError(userWithApp, checkUser.error)

        // make sure this app + app type is unique
        val checkAppAndType = IUserAppDao.checkForDupApp(userWithApp)
        if (checkAppAndType is ServiceResult.Error) {
            return userResponseError(userWithApp, checkAppAndType.error)
        }

        // generate the api key and insert the new app
        val apiKey = generateApiKey()
        val dbResult = IUserAppDao.insertUserApp(userWithApp.copy(
            apiKey = apiKey,
            appCreateDate = LocalDateTime.now().toDatabaseString())
        )

        if (dbResult is ServiceResult.Error)
            return userResponseError(userWithApp, dbResult.error)

        // if the app was inserted, return the app w/the user info
        return when (val newApp = IUserAppDao.getUserWithApp(apiKey)) {
                is ServiceResult.Error -> userResponseError(userWithApp, newApp.error)
                is ServiceResult.Success -> UserAppResponse(newApp.data)
        }

    }

    private fun userResponseError(userWithApp: UserWithApp, errorCode: ErrorCode): UserAppResponse {
        return UserAppResponse(userWithApp, listOf(ResponseErrors(errorCode, errorCode.message)))
    }
}