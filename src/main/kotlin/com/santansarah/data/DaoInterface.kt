package com.santansarah.data

import com.santansarah.utils.ServiceResult

/**
 * User DAO Interface.
 */
interface UserDao {
    suspend fun getUser(user: User): ServiceResult<User>
    suspend fun insertUser(user: User): ServiceResult<User>
}

/**
 * TODO: NOT IMPLEMENTED YET.
 */
interface UserAppDao {
    suspend fun doesApiKeyExist(apiKey: String): Boolean
    suspend fun getUserWithApp(apiKey: String): UserWithApp?
    suspend fun checkForDupApp(userApp: UserApp): ServiceResult<Boolean>
    suspend fun insertUserApp(userApp: UserApp): UserApp?

}