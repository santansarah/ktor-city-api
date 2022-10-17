package com.santansarah.plugins

import com.santansarah.data.dao.CityDaoImpl
import com.santansarah.data.dao.UserAppDaoImpl
import com.santansarah.data.dao.UserDaoImpl
import com.santansarah.domain.interfaces.ICityDao
import com.santansarah.domain.interfaces.IUserAppDao
import com.santansarah.domain.interfaces.IUserDao
import com.santansarah.domain.usecases.*
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
    single<IUserDao> { UserDaoImpl() }
    single { ValidateUserEmail() }
    single { InsertNewUser(get(), get()) }
    single { GetUser(get()) }

    single<IUserAppDao> { UserAppDaoImpl() }
    single { ValidateUserApp() }
    single { GenerateApiKey() }
    single { InsertNewUserApp(get(), get(), get(), get()) }

    single<ICityDao> { CityDaoImpl() }
}