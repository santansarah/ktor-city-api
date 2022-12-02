package com.santansarah.plugins

import com.santansarah.domain.interfaces.ICityDao
import com.santansarah.domain.usecases.*
import com.santansarah.routes.apps
import com.santansarah.routes.cities
import com.santansarah.routes.users
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        val insertService by inject<GetOrInsertUser>()
        val getUser by inject<GetUser>()
        users(insertService, getUser)

        val getUserApps by inject<GetUserApps>()
        val insertAppService by inject<InsertNewUserApp>()
        val updateUserApp by inject<UpdateUserApp>()
        apps(getUserApps, insertAppService, updateUserApp)

        /**
         * City Routes
         */
        val cityDao by inject<ICityDao>()
        cities(cityDao)
    }
}
