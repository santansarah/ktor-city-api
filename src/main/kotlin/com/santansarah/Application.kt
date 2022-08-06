package com.santansarah

import com.santansarah.data.DatabaseFactory
import com.santansarah.data.UserAppDao
import com.santansarah.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject


fun main() {

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        DatabaseFactory.init()

        configureStatusExceptions()
        configureKoin()
        configureRouting()
        configureSerialization()
        configureSecurity()

    }.start(wait = true)
}
