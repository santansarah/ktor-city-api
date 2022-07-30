package com.santansarah

import com.santansarah.data.DatabaseFactory
import com.santansarah.data.UserDao
import com.santansarah.data.UserDaoImpl
import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.domain.usecases.ValidateUserEmail
import com.santansarah.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.Koin
import io.ktor.server.application.*


fun main() {

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        DatabaseFactory.init()

        //val userDao: UserDao = UserDaoImpl()
        //val validateUserEmail = ValidateUserEmail()
        //val insertNewUser = InsertNewUser(validateUserEmail, userDao)
        configureKoin()
        configureRouting()
        configureSerialization()
        configureSecurity()

    }.start(wait = true)
}
