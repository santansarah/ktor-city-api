package com.santansarah.plugins

import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.data.models.UserWithApp
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.ktor.ext.inject

// principal for the app
data class AppPrincipal(val userWithApp: UserWithApp) : Principal
data class AuthPrincipal(val isValid: Boolean) : Principal

fun Application.configureSecurity() {

    // inject this at the Application level
    val IUserAppDao by inject<IUserAppDao>()

    // separate these Auth modules to keep this file clean.
    install(Authentication) {
        // and then api key provider
        configureApiKey(IUserAppDao)
        configureGoogleJWT(this@configureSecurity.environment.config)
        configureAppAuthority(this@configureSecurity.environment.config)
    }
}
