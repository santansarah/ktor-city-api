package com.santansarah.plugins

import com.santansarah.data.UserAppDao
import com.santansarah.data.UserWithApp
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ServiceResult
import dev.forst.ktor.apikey.apiKey
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.ktor.ext.inject

// principal for the app
data class AppPrincipal(val userWithApp: UserWithApp) : Principal

fun Application.configureSecurity() {

    val userAppDao by inject<UserAppDao>()

    install(Authentication) {
        // and then api key provider
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
}
