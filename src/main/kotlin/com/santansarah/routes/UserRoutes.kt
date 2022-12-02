package com.santansarah.routes

import com.santansarah.data.models.User
import com.santansarah.data.models.UserWithApp
import com.santansarah.data.models.toUser
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.domain.usecases.*
import com.santansarah.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * Gets or inserts a new [User] from Google JWT basic
 * account info.
 */
fun Route.users(
    getOrInsertUser: GetOrInsertUser,
    getUser: GetUser
) {

    route("users/authenticate") {
        authenticate("google") {
            get {

                val principal = call.principal<JWTPrincipal>()
                val expiresAt = principal!!.expiresAt?.time?.minus(System.currentTimeMillis())
                println("expires: $expiresAt")

                val request = principal.payload.toUser()

                /**
                 * Validate the [User] object and insert if everything's good.
                 */
                val userResponse = getOrInsertUser(request)
                var httpStatus =
                    if (userResponse.errors.isEmpty()) HttpStatusCode.OK else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus,
                    message = userResponse
                )

            }

        }
    }

    /**
     * Get a [User] by the userId that's stored in
     * UserPreferences, client side. Must be authenticated
     * with the API key that's authorized in the API config.
     */
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

