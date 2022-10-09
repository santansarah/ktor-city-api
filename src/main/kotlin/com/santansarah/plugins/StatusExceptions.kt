package com.santansarah.plugins

import com.auth0.jwt.exceptions.TokenExpiredException
import com.santansarah.data.AppType
import com.santansarah.data.User
import com.santansarah.data.UserApps
import com.santansarah.data.UserWithApp
import com.santansarah.domain.CityResponse
import com.santansarah.domain.ResponseErrors
import com.santansarah.domain.UserAppResponse
import com.santansarah.domain.UserResponse
import com.santansarah.utils.AppRoutes
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.GoogleException
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.configureStatusExceptions() {

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            println("path: ${call.request.path()}")
            println(cause)

            when (cause) {
                is SerializationException ->
                with(call.request.path()) {
                    when {
                        startsWith(AppRoutes.USERS_ROUTE) -> sendBadUser(call)
                        startsWith(AppRoutes.APPS_ROUTE) -> sendBadApp(call)
                    }
                }
                is AuthenticationException ->
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = CityResponse(errors =listOf(ResponseErrors(ErrorCode.INVALID_API_KEY,
                            ErrorCode.INVALID_API_KEY.message)))
                    )
                is GoogleException -> {
                    //call.response.headers.append(HttpHeaders.WWWAuthenticate, cause.realm)
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = UserResponse(
                            User(email = "sample@email.com"),
                            errors = listOf(
                                ResponseErrors(
                                    ErrorCode.INVALID_GOOGLE_CREDENTIALS,
                                    ErrorCode.INVALID_GOOGLE_CREDENTIALS.message
                                )
                            )
                        )
                    )
                }
                else ->
                    call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}

suspend fun sendBadApp(call: ApplicationCall) {
    val sampleApp = UserAppResponse(
        UserWithApp(
            email = "sample@email.com",
            appName = "Your App's Name", appType = AppType.DEVELOPMENT
        ),
        listOf(ResponseErrors(ErrorCode.INVALID_JSON, ErrorCode.INVALID_JSON.message))
    )

    call.respond(
        status = HttpStatusCode.BadRequest,
        message = sampleApp
    )
}

suspend fun sendBadUser(call: ApplicationCall) {
    val sampleUser = UserResponse(
        User(email = "sample@email.com"),
        listOf(ResponseErrors(ErrorCode.INVALID_JSON, ErrorCode.INVALID_JSON.message))
    )

    call.respond(
        status = HttpStatusCode.BadRequest,
        message = sampleUser
    )
}


