package com.santansarah

import com.santansarah.data.User
import com.santansarah.data.toUser
import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.utils.GoogleException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.serialization.SerializationException

fun Route.newAccount(
    insertNewUser: InsertNewUser
) {

    route("users/create") {
        authenticate("google") {
            post {

                val principal = call.principal<JWTPrincipal>()
                val expiresAt = principal!!.expiresAt?.time?.minus(System.currentTimeMillis())
                println("expires: $expiresAt")

                val request = principal.payload.toUser()

                /**
                 * Validate the [User] object and insert if everything's good.
                 */
                val userResponse = insertNewUser(request)
                var httpStatus =
                    if (userResponse.errors.isEmpty()) HttpStatusCode.Created else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus,
                    message = userResponse
                )

            }

        }
    }
}
