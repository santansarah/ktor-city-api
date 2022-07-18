package com.santansarah.domain.usecases

import com.santansarah.data.Client
import com.santansarah.data.ClientDao
import com.santansarah.domain.ClientErrors
import com.santansarah.domain.ClientResult

class ValidateClientEmail(
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

        if (client.email.isBlank() || !client.email.matches(emailAddressRegex))
            clientResult = ClientResult.Failure(ClientErrors.invalidEmail)

        return clientResult
    }

}