package com.santansarah.data

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

/**
 * I'm using SQLite. My [Users] table is created if it doesn't exist.
 * Because I modified my [Users] table in this branch, I used
 * [SchemaUtils.createMissingTablesAndColumns].
 */
object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:./cities"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction {
            SchemaUtils.create(Users, UserApps)
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }

    /**
     * Exposed transactions are blocking. Here I
     * start each query in its own coroutine and make
     * DB calls async on the [Dispatchers.IO] thread.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}