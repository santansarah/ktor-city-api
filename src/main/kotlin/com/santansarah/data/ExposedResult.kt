package com.santansarah.data

sealed class ExposedResult<T>(val data: T, val message: String? = null) {
    class Success<T>(data: T) : ExposedResult<T>(data)
    class Error<T>(data: T, message: String) : ExposedResult<T>(data, message)
}
