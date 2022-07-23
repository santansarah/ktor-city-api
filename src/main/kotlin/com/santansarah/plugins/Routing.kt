package com.santansarah.plugins

import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.newAccount
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(
    insertNewUser: InsertNewUser
) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        newAccount(insertNewUser)

    }
}
