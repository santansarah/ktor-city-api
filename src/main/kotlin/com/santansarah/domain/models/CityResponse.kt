package com.santansarah.domain.models

import com.santansarah.data.City
import com.santansarah.data.models.UserWithApp

@kotlinx.serialization.Serializable
data class CityResponse(
    val userWithApp: UserWithApp = UserWithApp(),
    val cities: List<City> = emptyList(),
    val errors: List<ResponseErrors> = emptyList()
)
