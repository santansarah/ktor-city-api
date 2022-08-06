package com.santansarah

import com.santansarah.data.CityDaoImpl
import com.santansarah.data.CityDaoInterface
import com.santansarah.domain.CityResponse
import com.santansarah.domain.ResponseErrors
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.cityRouting(
    cityDaoImpl: CityDaoInterface
) {
    route("cities") {
        authenticate {
            get {

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
                                message = CityResponse(dbResult.data)
                            )
                        }
                    }
                }
            }
        }
    }
}
