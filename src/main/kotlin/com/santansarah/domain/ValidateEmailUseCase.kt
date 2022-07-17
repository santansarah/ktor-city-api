package com.santansarah.domain

import com.santansarah.data.Client
import com.santansarah.data.ClientDao

class ValidateEmailUseCase(
    val clientDao: ClientDao
) {

    private val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    suspend operator fun invoke(client: Client): ClientResult {

        var clientResult: ClientResult = ClientResult.Success

        if (client.email.isNotEmpty() && client.email.matches(emailAddressRegex)) {
            //check to see if email exists with this client combo
            if (clientDao.doesClientExist(client))
                clientResult = ClientResult.Failure(ClientErrors.clientExists)
        } else {
            clientResult = ClientResult.Failure(ClientErrors.invalidEmail)
        }

        return clientResult
    }

}