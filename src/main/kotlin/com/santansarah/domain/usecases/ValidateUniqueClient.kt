package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.ClientDao
import com.santansarah.domain.ClientErrors
import com.santansarah.domain.ClientResult

class ValidateUniqueClient(
    val clientDao: ClientDao
) {

    suspend operator fun invoke(user: User): ClientResult {

        var clientResult: ClientResult = ClientResult.Success

        var dbClient = clientDao.doesClientExist(user)
        if (dbClient != null)
            clientResult = ClientResult.Failure(ClientErrors.clientExists)

        return clientResult
    }

}