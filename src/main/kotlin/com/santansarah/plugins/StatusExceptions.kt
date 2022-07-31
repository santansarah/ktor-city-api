package com.santansarah.plugins

import com.santansarah.data.User
import com.santansarah.domain.ResponseErrors
import com.santansarah.domain.UserResponse
import com.santansarah.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.configureStatusExceptions() {

    val sampleUser = UserResponse(User(email="sample@email.com"),
        listOf(ResponseErrors(ErrorCode.INVALID_USER, ErrorCode.INVALID_USER.message)))

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if(cause is SerializationException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = sampleUser
                )
            } else {
                call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
            }
        }
    }


}