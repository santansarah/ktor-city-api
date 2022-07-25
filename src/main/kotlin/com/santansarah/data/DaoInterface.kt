package com.santansarah.data


interface UserDao {
    suspend fun doesEmailExist(email: String): User?
    suspend fun getUser(user: User): ExposedResult<User>
    suspend fun insertUser(user: User): ExposedResult<User>
}

interface UserAppDao {
    suspend fun doesApiKeyExist(apiKey: String): Boolean
    suspend fun getUserWithApp(apiKey: String): UserWithApp?
    suspend fun doesAppExist(userApp: UserApp): ExposedResult<Boolean>
    suspend fun insertUserApp(userApp: UserApp): UserApp?

}