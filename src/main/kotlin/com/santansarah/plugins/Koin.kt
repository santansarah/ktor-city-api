package com.santansarah.plugins

import com.santansarah.data.UserDao
import com.santansarah.data.UserDaoImpl
import com.santansarah.domain.usecases.InsertNewUser
import com.santansarah.domain.usecases.ValidateUserEmail
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(cityModule)
    }
}

/**
 * [InsertNewUser] expects a Dao + ValidateUserEmail Use Case.
 * Here, I tell Koin how to create each...then get magically
 * injects them into the service.
 */
val cityModule = module {
    single<UserDao> { UserDaoImpl() }
    single { ValidateUserEmail() }
    single { InsertNewUser(get(), get()) }
}