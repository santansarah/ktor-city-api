package com.santansarah.plugins

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.server.auth.*
import io.ktor.server.config.*

/**
 * I created a User and [UserWithApp] specifically for my Android app.
 * When I call this from my Android app, I use the API key that
 * I got for my [UserWithApp]. This API key is also saved in the Ktor
 * app config file.
 */
fun AuthenticationConfig.configureAppAuthority(config: ApplicationConfig) {

    val appApiKey = config.property("ktor.appAuth.apiKey").getString()

    apiKey("app") {
        challenge {
            throw AuthenticationException()
        }

        validate { keyFromHeader ->
            if (keyFromHeader == appApiKey)
                AuthPrincipal(true)
            else
                null
        }
    }
}