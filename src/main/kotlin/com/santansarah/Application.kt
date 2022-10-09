package com.santansarah

import io.ktor.server.application.*
import com.santansarah.data.DatabaseFactory
import com.santansarah.data.UserAppDao
import com.santansarah.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

// Important: When you load from application.conf, don't use
// embeddedServer(). Instead, your fun main above will
// handle the port and host. Otherwise, your Application
// extension methods won't have the environment!

fun Application.module() {

        DatabaseFactory.init()

        configureStatusExceptions()
        configureKoin()
        configureSecurity()
        configureRouting()
        configureSerialization()
}
