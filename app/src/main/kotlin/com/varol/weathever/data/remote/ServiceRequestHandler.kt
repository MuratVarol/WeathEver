package com.varol.weathever.data.remote

import com.google.gson.JsonParseException
import com.squareup.moshi.JsonDataException
import com.varol.weathever.internal.util.Failure
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * Singleton class for handling API requests and responses
 */
object ServiceRequestHandler {

    /**
     * Handles all requests and responses with success, errors and exceptions,
     * fills data to Result data holder according to response type.
     * *** Only Rx Single type supported.
     */
    fun <T> sendRequest(call: Single<T>): Single<Either<Failure, T>> {
        return call
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map<Either<Failure, T>> {
                Either.Right(it)
            }
            .onErrorResumeNext { throwable: Throwable ->
                Single.just(
                    Either.Left(handleParsingErrors(throwable))
                )
            }
            .doOnError { throwable: Throwable -> Either.Left(handleParsingErrors(throwable)) }
    }
}

private fun handleParsingErrors(throwable: Throwable): Failure {
    return when (throwable) {
        is JsonParseException, is JsonDataException ->
            Failure.ParsingDataError
        else -> Failure.UnknownError()
    }
}
