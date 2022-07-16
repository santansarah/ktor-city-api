package com.santansarah.data

import org.jetbrains.exposed.sql.ResultRow

class ClientDaoImpl: ClientDao {

    /**
     * map our table to a [Client] object
     */
    private fun resultRowToClient(row: ResultRow) = Client(
        id = row[Clients.id],
        email = row[Clients.email],
        appName = row[Clients.appName],
        appType = row[Clients.appType],
        apiKey = row[Clients.apiKey]
    )

    override suspend fun doesApiKeyExist(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun doesClientExist(client: Client): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun insertClient(client: Client): Client {
        TODO("Not yet implemented")
    }

    override suspend fun verifyApiKey(client: Client): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateClient(client: Client): Client {
        TODO("Not yet implemented")
    }

    override suspend fun deleteClient(client: Client): Boolean {
        TODO("Not yet implemented")
    }

}