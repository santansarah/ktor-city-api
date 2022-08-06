package com.santansarah.domain

import com.santansarah.data.City
import com.santansarah.data.User
import com.santansarah.data.UserApp
import com.santansarah.data.UserWithApp
import com.santansarah.utils.ErrorCode

@kotlinx.serialization.Serializable
data class CityResponse(
    val cities: List<City> = emptyList(),
    val errors: List<ResponseErrors> = emptyList()
)
