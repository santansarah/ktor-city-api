package com.santansarah.data

import com.santansarah.data.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class ClientDaoImpl : ClientDao {

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

    override suspend fun doesClientExist(client: Client): Client? {
        return dbQuery {
            Clients.select {
                (Clients.email eq client.email) and
                        (Clients.appName eq client.appName) and
                        (Clients.appType eq client.appType)
            }
                .map(::resultRowToClient)
                .singleOrNull()
        }
    }

    override suspend fun insertClient(client: Client): Client? {
        //var statement : InsertStatement<Number>? = null
        return dbQuery {
            Clients
                .insert {
                    it[email] = client.email
                    it[appName] = client.appName
                    it[appType] = client.appType
                    it[apiKey] = client.apiKey
                }
                .resultedValues?.singleOrNull()?.let {
                    resultRowToClient(it)
                }
        }
        //return statement?.resultedValues?.singleOrNull()?.let(::resultRowToClient)
    }

    override suspend fun getClient(apiKey: String): Client? = dbQuery {
        Clients
            .select { Clients.apiKey eq apiKey }
            .map(::resultRowToClient)
            .singleOrNull()
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