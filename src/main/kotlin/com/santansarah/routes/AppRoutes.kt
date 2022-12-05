package com.santansarah.routes

import com.santansarah.data.models.UserWithApp
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.domain.models.UserAppResponse
import com.santansarah.domain.usecases.*
import com.santansarah.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.apps(
    getUserApps: GetUserApps,
    insertNewUserApp: InsertNewUserApp,
    updateUserApp: UpdateUserApp
) {

    route("apps/{userId}") {
        authenticate("app") {
            get {

                val userId = call.parameters["userId"]?.toInt() ?: 0

                if (userId == 0) {
                    call.respond(
                        status = HttpStatusCode.BadRequest, message = UserAppResponse(
                            listOf(UserWithApp()), listOf(
                                ResponseErrors(
                                    ErrorCode.UNKNOWN_USER, ErrorCode.UNKNOWN_USER.message
                                )
                            )
                        )
                    )
                }

                val userWithAppResponse = getUserApps(userId(userId))
                var httpStatus =
                    if (userWithAppResponse.errors.isEmpty()) HttpStatusCode.OK else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus, message = userWithAppResponse
                )

            }
        }
    }

    route("apps/create") {
        authenticate("app") {
            post {
                val userWithApp = call.receive<UserWithApp>()

                /**
                 * Validate the [UserWithApp] object and insert if everything's good.
                 */
                val userResponse = insertNewUserApp(userWithApp)
                var httpStatus = if (userResponse.errors.isEmpty()) HttpStatusCode.Created
                else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus, message = userResponse
                )
            }
        }
    }

    route("app/{id}") {
        authenticate("app") {
            get {
                val appId = call.parameters["id"]?.toInt() ?: 0
                verifyAppId(appId)

                /**
                 * Get the [UserWithApp] object by appId.
                 */
                val appResponse = getUserApps(appId(appId))
                var httpStatus = if (appResponse.errors.isEmpty()) HttpStatusCode.OK
                else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus, message = appResponse
                )
            }
        }
    }

    route("app/{id}") {
        authenticate("app") {
            patch {
                val appId = call.parameters["id"]?.toInt() ?: 0
                verifyAppId(appId)
                val userWithApp = call.receive<UserWithApp>()

                /**
                 * Get the [UserWithApp] object by appId.
                 */
                val appResponse = updateUserApp(userWithApp.copy(userAppId = appId))
                var httpStatus = if (appResponse.errors.isEmpty()) HttpStatusCode.OK
                else HttpStatusCode.BadRequest

                call.respond(
                    status = httpStatus, message = appResponse
                )
            }
        }
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.verifyAppId(
    appId: Int
) {
    if (appId == 0) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = UserAppResponse(
                apps = listOf(UserWithApp()),
                errors = listOf(
                    ResponseErrors(
                        code = ErrorCode.UNKNOWN_USER,
                        message = ErrorCode.UNKNOWN_USER.message
                    )
                )
            )
        )
    }
}
