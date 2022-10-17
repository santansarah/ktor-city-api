package com.santansarah.routes

import com.santansarah.data.models.User
import com.santansarah.data.models.toUser
import com.santansarah.domain.usecases.*
import com.santansarah.plugins.AppPrincipal
import com.santansarah.plugins.AuthPrincipal
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.pipeline.*

fun Route.users(
    insertNewUser: InsertNewUser,
    getUser: GetUser
) {

    route("users/create") {
        authenticate("google") {
            get {

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

    route("users/get") {
        authenticate("google") {
            get {

                val principal = call.principal<JWTPrincipal>()
                val expiresAt = principal!!.expiresAt?.time?.minus(System.currentTimeMillis())
                println("expires: $expiresAt")

                val request = principal.payload.toUser()
                /**
                 * Get the [User] from the email address in the payload.
                 */
                val userResponse = getUser(Email(request.email))
                var httpStatus =
                    if (userResponse.errors.isEmpty()) HttpStatusCode.Found else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus,
                    message = userResponse
                )

            }

        }
    }

    route("users/{id}") {
        authenticate("app") {
            get {

                println("user route called")

                //val principal = call.principal<AuthPrincipal>()!!

                val userId = call.parameters["id"]?.toInt() ?: 0
                /*if (userId == 0) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = User()
                    )
                }*/

                val userResponse = getUser(Id(userId))
                var httpStatus =
                    if (userResponse.errors.isEmpty()) HttpStatusCode.OK else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus,
                    message = userResponse
                )

            }
        }
    }
}

