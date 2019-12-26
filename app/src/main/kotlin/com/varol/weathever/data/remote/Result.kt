package com.varol.weathever.data.remote

/**
 * Cascade class of all API response
 * Success if incoming data is valid
 * Error if incoming data is non-parcelable or Exception from service
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val error: Throwable) : Result<T>()
}