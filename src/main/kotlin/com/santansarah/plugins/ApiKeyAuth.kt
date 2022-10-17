package com.santansarah.plugins

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.server.auth.*

fun AuthenticationConfig.configureApiKey(IUserAppDao: IUserAppDao) {

    apiKey("city") {
        challenge {
            throw AuthenticationException()
        }

        /**
         * get the [UserWithApp] from the api key. if it comes back with
         * a match, send the AppPrincipal & authenticate.
         */
        validate { keyFromHeader ->
            when (val userWithApp = IUserAppDao.getUserWithApp(keyFromHeader)) {
                is ServiceResult.Error -> null
                is ServiceResult.Success -> AppPrincipal(userWithApp.data)
            }
        }
    }
}