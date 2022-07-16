package com.santansarah

import com.santansarah.data.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.santansarah.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
