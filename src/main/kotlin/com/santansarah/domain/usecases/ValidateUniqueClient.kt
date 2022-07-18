package com.santansarah.domain.usecases

import com.santansarah.data.Client
import com.santansarah.data.ClientDao
import com.santansarah.domain.ClientErrors
import com.santansarah.domain.ClientResult

class ValidateUniqueClient(
    val clientDao: ClientDao
) {

    suspend operator fun invoke(client: Client): ClientResult {

        var clientResult: ClientResult = ClientResult.Success

        var dbClient = clientDao.doesClientExist(client)
        if (dbClient != null)
            clientResult = ClientResult.Failure(ClientErrors.clientExists)

        return clientResult
    }

}