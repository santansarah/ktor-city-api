package com.santansarah.data

import com.santansarah.utils.ErrorCode


/*
sealed class ExposedResult<T>(val data: T, val appErrorCodes: ErrorCode? = null) {
    class Success<T>(data: T) : ExposedResult<T>(data)
    class Error<T>(data: T, appErrorCodes: ErrorCode) : ExposedResult<T>(data, appErrorCodes)
}
*/
