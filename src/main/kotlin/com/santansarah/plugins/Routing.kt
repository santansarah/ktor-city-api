package com.santansarah.plugins

import com.santansarah.routes.cityRouting
import com.santansarah.domain.interfaces.ICityDao
import com.santansarah.domain.usecases.GetUser
import com.santansarah.domain.usecases.GetOrInsertUser
import com.santansarah.domain.usecases.InsertNewUserApp
import com.santansarah.routes.users
import com.santansarah.routes.newApp
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Lazy inject from within a Ktor Routing Node
        val insertService by inject<GetOrInsertUser>()
        val getUser by inject<GetUser>()

        /**
         * User routes.
         */
        users(insertService, getUser)

        /**
         * UserApp Routes
         */
        val insertAppService by inject<InsertNewUserApp>()
         newApp(insertAppService)

        /**
         * City Routes
         */
        val cityDao by inject<ICityDao>()
        cityRouting(cityDao)
    }
}
