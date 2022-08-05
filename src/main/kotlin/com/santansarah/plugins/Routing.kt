package com.santansarah.plugins

import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.domain.usecases.InsertNewUserApp
import com.santansarah.newAccount
import com.santansarah.newApp
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Lazy inject HelloService from within a Ktor Routing Node
        val insertService by inject<InsertNewUser>()

        /**
         * User routes.
         */
        newAccount(insertService)

        /**
         * UserApp Routes
         */
        val insertAppService by inject<InsertNewUserApp>()
         newApp(insertAppService)
    }
}
