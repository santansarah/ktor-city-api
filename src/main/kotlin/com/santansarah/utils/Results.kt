package com.santansarah.utils

/**
 * Use case results.
 */
sealed class UseCaseResult {
    object Success : UseCaseResult()
    data class Failure(val error: AppErrorCodes) : UseCaseResult()
}


