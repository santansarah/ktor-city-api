package com.santansarah.data

@kotlinx.serialization.Serializable
data class City(
    val zip: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val city: String = "",
    val state: String = "",
    val population: Int = 0
)
