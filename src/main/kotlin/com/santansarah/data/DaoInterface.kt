package com.santansarah.data


interface UserDao {
    suspend fun doesUserExist(email: String): User?
    suspend fun insertUser(user: User): User?
    suspend fun getUserWithApp(apiKey: String): UserWithApp?
}

interface UserAppDao {
    suspend fun doesApiKeyExist(): Boolean
    suspend fun insertUserApp(): UserApp?

}