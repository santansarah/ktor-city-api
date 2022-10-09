package com.santansarah.plugins

import com.santansarah.data.UserAppDao
import com.santansarah.data.UserWithApp
import io.ktor.client.engine.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.ktor.ext.inject

// principal for the app
data class AppPrincipal(val userWithApp: UserWithApp) : Principal

fun Application.configureSecurity() {

    // inject this at the Application level
    val userAppDao by inject<UserAppDao>()

    // separate these Auth modules to keep this file clean.
    install(Authentication) {
        // and then api key provider
        configureApiKey(userAppDao)
        configureGoogleJWT(this@configureSecurity.environment.config)
    }
}
