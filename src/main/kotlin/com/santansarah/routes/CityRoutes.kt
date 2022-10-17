package com.santansarah.routes

import com.santansarah.domain.interfaces.ICityDao
import com.santansarah.domain.models.CityResponse
import com.santansarah.domain.models.ResponseErrors
import com.santansarah.plugins.AppPrincipal
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.cityRouting(
    cityDaoImpl: ICityDao
) {
    route("cities") {
        authenticate("city") {
            get {

                // we know we're not null, b/c we're authenticated
                // at this point. something that would be really
                // neat here would be to insert this call into
                // the db to track queries by api key. we could then
                // get queries per second, etc.
                val principal = call.principal<AppPrincipal>()!!
                val userWithApp = principal.userWithApp

                val namePrefix: String? = call.request.queryParameters["name"]
                val zipPrefix: String? = call.request.queryParameters["zip"]

                if (namePrefix.isNullOrBlank() && zipPrefix.isNullOrBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = ResponseErrors(
                            ErrorCode.INVALID_CITY_QUERY,
                            ErrorCode.INVALID_CITY_QUERY.message
                        )
                    )
                }

                // TODO: Create Use Case and do better checks.
                namePrefix?.let {
                    when (val dbResult = cityDaoImpl.getCitiesByName(it)) {
                        is ServiceResult.Error -> {
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = CityResponse(
                                    userWithApp = userWithApp,
                                    errors = listOf(
                                        ResponseErrors(
                                            dbResult.error,
                                            dbResult.error.message
                                        )
                                    )
                                )
                            )
                        }
                        is ServiceResult.Success -> {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = CityResponse(userWithApp, dbResult.data)
                            )
                        }
                    }
                }
            }
        }
    }
}
