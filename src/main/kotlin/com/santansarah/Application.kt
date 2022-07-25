package com.santansarah

import com.santansarah.data.DatabaseFactory
import com.santansarah.data.UserDao
import com.santansarah.data.UserDaoImpl
import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.domain.usecases.ValidateUserEmail
import com.santansarah.plugins.configureRouting
import com.santansarah.plugins.configureSecurity
import com.santansarah.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        DatabaseFactory.init()

        val userDao: UserDao = UserDaoImpl()
        val validateUserEmail = ValidateUserEmail()
        val insertNewUser = InsertNewUser(validateUserEmail, userDao)

        configureRouting(insertNewUser)
        configureSerialization()
        configureSecurity()

    }.start(wait = true)
}
