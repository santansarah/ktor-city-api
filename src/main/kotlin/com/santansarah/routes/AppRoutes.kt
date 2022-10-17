package com.santansarah.routes

import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.usecases.InsertNewUserApp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.newApp(
    insertNewUserApp: InsertNewUserApp
) {
    post("apps/create") {
        /**
         * Return if the object is null.
         */
        val request = call.receiveOrNull<UserWithApp>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        /**
         * Validate the [UserWithApp] object and insert if everything's good.
         */
        val userResponse = insertNewUserApp(request)
        var httpStatus = if (userResponse.errors.isEmpty()) HttpStatusCode.Created else HttpStatusCode.BadRequest

        call.respond(
            status = httpStatus,
            message = userResponse
        )
    }

}
