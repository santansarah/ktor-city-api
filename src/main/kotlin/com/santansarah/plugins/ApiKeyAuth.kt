package com.santansarah.plugins

import com.santansarah.data.UserAppDao
import com.santansarah.data.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.server.auth.*
import io.ktor.server.config.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun AuthenticationConfig.configureApiKey(userAppDao: UserAppDao) {

    apiKey {
        challenge {
            throw AuthenticationException()
        }

        /**
         * get the [UserWithApp] from the api key. if it comes back with
         * a match, send the AppPrincipal & authenticate.
         */
        validate { keyFromHeader ->
            when (val userWithApp = userAppDao.getUserWithApp(keyFromHeader)) {
                is ServiceResult.Error -> null
                is ServiceResult.Success -> AppPrincipal(userWithApp.data)
            }
        }
    }
}