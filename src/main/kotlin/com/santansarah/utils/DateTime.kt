package com.santansarah.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"

fun LocalDateTime.toDatabaseString(): String {
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return formatter.format(this)
}