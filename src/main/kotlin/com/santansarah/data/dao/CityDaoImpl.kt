package com.santansarah.data.dao

import com.santansarah.data.Cities
import com.santansarah.data.models.City
import com.santansarah.domain.interfaces.ICityDao
import com.santansarah.data.DatabaseFactory.dbQuery
import com.santansarah.utils.ErrorCode
import com.santansarah.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

class CityDaoImpl : ICityDao {

    /**
     * Maps my [ResultRow] to a [City] object.
     */
    private fun resultRowToCity(row: ResultRow): City {
      return try {
            City(
                zip = row[Cities.zip],
                lat = row[Cities.lat],
                lng = row[Cities.lng],
                city = row[Cities.city],
                state = row[Cities.state],
                population = row[Cities.population],
                county = row[Cities.county]
            )
        } catch (e: Exception) {
            // turns out population was null for 1074 fields
            // it was throwing an error. i did a db update to fix.
            println(e)
          return City()
        }
    }

    override suspend fun getCitiesByName(prefix: String): ServiceResult<List<City>> {
        return try {
            val minPopulation = 20000
            val cities = dbQuery {
                Cities.select {
                    (Cities.city like "$prefix%") and (Cities.population greaterEq minPopulation)
                }.orderBy(Cities.city to SortOrder.ASC)
                    .map(::resultRowToCity)
            }

            ServiceResult.Success(cities)

        } catch (e: Exception) {
            println(e)
            when (e) {
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getCitiesByZip(prefix: String): ServiceResult<List<City>> {
        return try {
            val cities = dbQuery {
                Cities.select {
                    Cities.zip.castTo<String>(VarCharColumnType()) like "$prefix%"
                }.map(::resultRowToCity)
            }

            ServiceResult.Success(cities)

        } catch (e: Exception) {
            println(e)
            when (e) {
                is ExposedSQLException -> {
                    println("exception from insert function: ${e.errorCode}")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getCityByZip(zipCode: Int): ServiceResult<City> {
        return try {
            val dbCity = dbQuery {
                Cities.select {
                    Cities.zip eq zipCode
                }.map(::resultRowToCity)
                    .single()
            }
            ServiceResult.Success(dbCity)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> {
                    ServiceResult.Error(ErrorCode.UNKNOWN_CITY)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }
}