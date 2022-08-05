package com.santansarah.domain.usecases

import com.santansarah.data.UserApp
import com.santansarah.data.UserAppDao
import com.santansarah.data.UserDao
import com.santansarah.data.UserWithApp
import com.santansarah.domain.ResponseErrors
import com.santansarah.domain.UserAppResponse
import com.santansarah.domain.UserResponse
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import com.santansarah.utils.toDatabaseString
import java.time.LocalDateTime

class InsertNewUserApp
    (
    private val validateUserApp: ValidateUserApp,
    private val generateApiKey: GenerateApiKey,
    private val userAppDao: UserAppDao,
    private val userDao: UserDao
) {

    suspend operator fun invoke(userWithApp: UserWithApp): UserAppResponse {

        // check for blank fields
        val validateAppResult = validateUserApp(userWithApp)
        if (validateAppResult is ServiceResult.Error) {
            return userResponseError(userWithApp, validateAppResult.error)
        }

        // make sure the user exists
        val checkUser = userDao.doesUserExist(userWithApp.userId, userWithApp.email)
        if (checkUser is ServiceResult.Error)
            return userResponseError(userWithApp, checkUser.error)

        // make sure this app + app type is unique
        val checkAppAndType = userAppDao.checkForDupApp(userWithApp)
        if (checkAppAndType is ServiceResult.Error) {
            return userResponseError(userWithApp, checkAppAndType.error)
        }

        // generate the api key and insert the new app
        val apiKey = generateApiKey()
        val dbResult = userAppDao.insertUserApp(userWithApp.copy(
            apiKey = apiKey,
            appCreateDate = LocalDateTime.now().toDatabaseString())
        )

        if (dbResult is ServiceResult.Error)
            return userResponseError(userWithApp, dbResult.error)

        // if the app was inserted, return the app w/the user info
        return when (val newApp = userAppDao.getUserWithApp(apiKey)) {
                is ServiceResult.Error -> userResponseError(userWithApp, newApp.error)
                is ServiceResult.Success -> UserAppResponse(newApp.data)
        }

    }

    private fun userResponseError(userWithApp: UserWithApp, errorCode: ErrorCode): UserAppResponse {
        return UserAppResponse(userWithApp, listOf(ResponseErrors(errorCode, errorCode.message)))
    }
}