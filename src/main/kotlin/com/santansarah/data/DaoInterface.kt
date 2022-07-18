package com.santansarah.data


interface ClientDao {
    suspend fun doesApiKeyExist(): Boolean
    suspend fun doesClientExist(client: Client): Client?
    suspend fun insertClient(client: Client): Client?
    suspend fun verifyApiKey(client: Client): Boolean
    suspend fun updateClient(client: Client): Client
    suspend fun deleteClient(client: Client): Boolean
    suspend fun getClient(apiKey: String): Client?
}