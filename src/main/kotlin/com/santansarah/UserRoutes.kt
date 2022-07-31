package com.santansarah

import com.santansarah.data.User
import com.santansarah.domain.usecases.InsertNewUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.newAccount(
    insertNewUser: InsertNewUser
) {
    post("users/create") {
        /**
         * Return if the user object is null.
         */
        val request = call.receiveOrNull<User>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        /**
         * Validate the [User] object and insert if everything's good.
         */
        val userResponse = insertNewUser(request)
        var httpStatus = if (userResponse.errors.isEmpty()) HttpStatusCode.Created else HttpStatusCode.BadRequest

        call.respond(
            status = httpStatus,
            message = userResponse
        )
    }

}
