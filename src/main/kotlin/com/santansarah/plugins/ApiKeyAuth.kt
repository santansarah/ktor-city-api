package com.santansarah.plugins

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.server.auth.*

/**
 * Get the [UserWithApp] from an API key. If it comes back with
 * a match, send the AppPrincipal & authenticate.
 */
fun AuthenticationConfig.configureApiKey(userAppDao: IUserAppDao) {

    apiKey("city") {
        challenge {
            throw AuthenticationException()
        }

        validate { keyFromHeader ->
            when (val userWithApp = userAppDao.getUserWithApp(keyFromHeader)) {
                is ServiceResult.Error -> null
                is ServiceResult.Success -> AppPrincipal(userWithApp.data)
            }
        }
    }
}