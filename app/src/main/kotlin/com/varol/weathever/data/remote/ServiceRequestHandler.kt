package com.varol.weathever.data.remote

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Singleton class for handling API requests and responses
 */
object ServiceRequestHandler {

    /**
     * Handles all requests and responses with success, errors and exceptions,
     * fills data to Result data holder according to response type.
     * *** Only Rx Single type supported.
     */
    fun <T> sendRequest(call: Single<T>): Single<Result<T>> {
        return call
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .map<Result<T>> {
                Result.Success(it)
            }
            .onErrorResumeNext { throwable: Throwable ->
                Single.just(
                    Result.Error(throwable)
                )
            }
            .doOnError { t: Throwable -> Result.Error<T>(t) }
    }
}
