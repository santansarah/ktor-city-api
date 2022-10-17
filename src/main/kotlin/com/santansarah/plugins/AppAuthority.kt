package com.santansarah.plugins

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.server.auth.*
import io.ktor.server.config.*

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