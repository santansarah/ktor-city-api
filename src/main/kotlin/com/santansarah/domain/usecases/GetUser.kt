package com.santansarah.domain.usecases

import com.santansarah.data.models.User
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserResponse
import com.santansarah.utils.ServiceResult

sealed class GetUserBy
data class Id(val id: Int): GetUserBy()
data class Email(val email: String) : GetUserBy()

class GetUser
    (
    private val userDao: IUserDao
) {

    suspend operator fun invoke(getUserBy: GetUserBy): UserResponse {
        return when(getUserBy) {
            is Id -> getUserById(getUserBy.id)
            is Email -> getUserByEmail(getUserBy.email)
        }
    }

    private suspend fun getUserById(id: Int): UserResponse {
        return when (val dbResult = userDao.getUserById(id)) {
            is ServiceResult.Success -> UserResponse(dbResult.data, emptyList())
            is ServiceResult.Error -> UserResponse(
                User(),
                listOf(ResponseErrors(dbResult.error, dbResult.error.message))
            )
        }
    }

    private suspend fun getUserByEmail(email: String): UserResponse {
        return when (val dbResult = userDao.getUserByEmail(email)) {
            is ServiceResult.Success -> UserResponse(dbResult.data, emptyList())
            is ServiceResult.Error -> UserResponse(
                User(),
                listOf(ResponseErrors(dbResult.error, dbResult.error.message))
            )
        }
    }
}