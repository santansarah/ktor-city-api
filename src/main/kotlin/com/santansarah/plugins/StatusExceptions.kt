package com.santansarah.plugins

import com.santansarah.data.models.AppType
import com.santansarah.data.models.User
import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.models.CityResponse
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.domain.models.UserResponse
import com.santansarah.utils.AppRoutes
import com.santansarah.utils.AuthenticationException
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.GoogleException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

/**
 * I use the [StatusPages] plugin to provide consistent
 * error handling for all of my routes.
 */
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
                        message = CityResponse(errors =listOf(
                            ResponseErrors(ErrorCode.INVALID_API_KEY,
                            ErrorCode.INVALID_API_KEY.message)
                        ))
                    )
                /**
                 * If JWT validation fails and a GoogleException is thrown,
                 * the API returns an Unauthorized status code, along with the
                 * INVALID_GOOGLE_CREDENTIALS [ErrorCode] in the [ResponseErrors]
                 * data class.
                 */
                is GoogleException -> {
                    //call.response.headers.append(HttpHeaders.WWWAuthenticate, cause.realm)
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = UserResponse(
                            User(),
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


