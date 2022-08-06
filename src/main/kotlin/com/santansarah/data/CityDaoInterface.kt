package com.santansarah.data

import com.santansarah.utils.ServiceResult

/**
 * DAO Interface.
 */
interface CityDaoInterface {
    suspend fun getCitiesByName(prefix: String): ServiceResult<List<City>>
    suspend fun getCitiesByZip(prefix: String): ServiceResult<List<City>>
}

