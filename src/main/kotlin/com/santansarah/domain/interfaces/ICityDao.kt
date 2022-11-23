package com.santansarah.domain.interfaces

import com.santansarah.data.models.City
import com.santansarah.utils.ServiceResult

/**
 * DAO Interface.
 */
interface ICityDao {
    suspend fun getCitiesByName(prefix: String): ServiceResult<List<City>>
    suspend fun getCitiesByZip(prefix: String): ServiceResult<List<City>>
    suspend fun getCityByZip(zipCode: Int): ServiceResult<City>
}

