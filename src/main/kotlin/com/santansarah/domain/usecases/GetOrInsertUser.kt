package com.santansarah.domain.usecases

import com.santansarah.data.models.User
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserResponse
import com.santansarah.utils.ServiceResult
import com.santansarah.utils.toDatabaseString
import java.time.LocalDateTime

/**
 * Returns an existing [User] by email if they exist.
 * Otherwise, inserts the new [User] with info that we
 * got from the Google JWT.
 */
class GetOrInsertUser
    (
    private val userDao: IUserDao
) {
    suspend operator fun invoke(user: User): UserResponse {

        val existingUser = userDao.getUserByEmail(user.email)
        if (existingUser is ServiceResult.Success)
            return UserResponse(existingUser.data, emptyList())

        return when(val newUser = userDao.insertUser(user.copy(
            userCreateDate = LocalDateTime.now().toDatabaseString())
        )) {
            is ServiceResult.Success -> UserResponse(newUser.data, emptyList())
            is ServiceResult.Error -> UserResponse(user, listOf(ResponseErrors(newUser.error,
                newUser.error.message)))
        }

    }
}