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

fun Application.configureSecurity() {

    val userAppDao by inject<UserAppDao>()

    // principal for the app
    data class AppPrincipal(val userWithApp: UserWithApp) : Principal

	install(Authentication) {
		// and then api key provider
		apiKey {
            challenge {
                throw AuthenticationException()
            }
			// set function that is used to verify request
			validate { keyFromHeader ->

                when(val userWithApp = userAppDao.getUserWithApp(keyFromHeader)) {
                    is ServiceResult.Error -> null
                    is ServiceResult.Success -> {
                        keyFromHeader
                            .takeIf { it == userWithApp.data.apiKey }
                            ?.let { AppPrincipal(userWithApp.data) }
                    }
                }
			}
		}
	}
}
