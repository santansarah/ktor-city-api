package com.santansarah.utils

sealed class ServiceResult<out T> {
    data class Success<out T>(val data: T) : ServiceResult<T>()

    data class Error(val error: ErrorCode) : ServiceResult<Nothing>()
}
