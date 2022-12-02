package com.santansarah.domain.usecases

import com.santansarah.data.models.User
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.domain.models.UserResponse
import com.santansarah.utils.ServiceResult

sealed class GetUserAppBy
data class userId(val userId: Int): GetUserAppBy()
data class appId(val appId: Int) : GetUserAppBy()

class GetUserApps
    (
    private val userAppDao: IUserAppDao
) {

    suspend operator fun invoke(getUserAppBy: GetUserAppBy): UserAppResponse {
        return when(getUserAppBy) {
            is userId -> getUserAppsByUserId(getUserAppBy.userId)
            is appId -> getUserAppsByAppId(getUserAppBy.appId)
        }
    }

    private suspend fun getUserAppsByUserId(userId: Int): UserAppResponse {
        return when (val dbResult = userAppDao.getUserAppsByUserId(userId)) {
            is ServiceResult.Success -> UserAppResponse(dbResult.data, emptyList())
            is ServiceResult.Error -> UserAppResponse(
                listOf(UserWithApp()),
                listOf(ResponseErrors(dbResult.error, dbResult.error.message))
            )
        }
    }

    private suspend fun getUserAppsByAppId(appId: Int): UserAppResponse {
        return when (val dbResult = userAppDao.getAppById(appId)) {
            is ServiceResult.Success -> UserAppResponse(
                listOf(dbResult.data),
                emptyList())
            is ServiceResult.Error -> UserAppResponse(
                listOf(UserWithApp()),
                listOf(ResponseErrors(dbResult.error, dbResult.error.message))
            )
        }
    }

}