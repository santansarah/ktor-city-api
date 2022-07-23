package com.santansarah.data


interface UserDao {
    suspend fun doesUserExist(email: String): User?
    suspend fun insertUser(user: User): ExposedResult<User>
    suspend fun getUserWithApp(apiKey: String): UserWithApp?
}

interface UserAppDao {
    suspend fun doesApiKeyExist(apiKey: String): Boolean
    suspend fun insertUserApp(userApp: UserApp): UserApp?

}