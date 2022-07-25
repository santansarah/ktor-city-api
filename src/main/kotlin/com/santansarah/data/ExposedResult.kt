package com.santansarah.data

import com.santansarah.utils.AppErrorCodes

sealed class ExposedResult<T>(val data: T, val appErrorCodes: AppErrorCodes? = null) {
    class Success<T>(data: T) : ExposedResult<T>(data)
    class Error<T>(data: T, appErrorCodes: AppErrorCodes) : ExposedResult<T>(data, appErrorCodes)
}
