package com.varol.weathever.internal.util.network

import com.varol.weathever.internal.util.Failure
import com.varol.weathever.internal.util.NetworkHandler
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandlerInterceptor(private val networkHandler: NetworkHandler) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkHandler.isConnected)
            throw Failure.NoConnectivityError

        val response = try {
            chain.proceed(chain.request())
        } catch (ex: Exception) {
            throw when (ex) {
                is UnknownHostException, is IllegalArgumentException -> Failure.UnknownHostError()
                is HttpException -> Failure.HttpError(ex.code(), ex.message())
                is SocketTimeoutException -> Failure.TimeOutError(ex.message)
                else -> IOException(ex)
            }
        }
        return when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    response
                } ?: run {
                    throw Failure.EmptyResponse
                }
            }
            false -> throw Failure.ApiError(response.code(), response.message())
        }
    }

}