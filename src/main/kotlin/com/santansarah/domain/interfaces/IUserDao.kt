package com.santansarah.domain.interfaces

import com.santansarah.data.models.User
import com.santansarah.data.models.UserApp
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.ServiceResult

/**
 * [User] DAO Interface.
 */
interface IUserDao {
    suspend fun doesUserExist(userId: Int, email: String): ServiceResult<Boolean>
    suspend fun insertUser(user: User): ServiceResult<User>
    suspend fun getUserByEmail(email: String): ServiceResult<User>
    suspend fun getUserById(id: Int): ServiceResult<User>
}

/**
 * [UserApp] interface.
 */
interface IUserAppDao {
    suspend fun getUserWithApp(apiKey: String): ServiceResult<UserWithApp>
    suspend fun checkForDupApp(userWithApp: UserWithApp): ServiceResult<Boolean>
    suspend fun insertUserApp(userWithApp: UserWithApp): ServiceResult<UserApp>

}