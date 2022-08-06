package com.santansarah.data

import com.santansarah.utils.ServiceResult

/**
 * [User] DAO Interface.
 */
interface UserDao {
    suspend fun doesUserExist(userId: Int, email: String): ServiceResult<Boolean>
    suspend fun insertUser(user: User): ServiceResult<User>
}

/**
 * [UserApp] interface.
 */
interface UserAppDao {
    suspend fun getUserWithApp(apiKey: String): ServiceResult<UserWithApp>
    suspend fun checkForDupApp(userWithApp: UserWithApp): ServiceResult<Boolean>
    suspend fun insertUserApp(userWithApp: UserWithApp): ServiceResult<UserApp>

}