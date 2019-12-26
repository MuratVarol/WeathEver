package com.varol.weathever.internal.util

import java.io.IOException

sealed class Failure : IOException() {
    class ServerError(var code: Int = 0, override var message: String) : Failure()
    class JsonError : Failure()
    class ApiError(var code: Int, override var message: String) : Failure()
    class UnknownError(override var message: String? = null) : Failure()
    class HttpError(var code: Int, override var message: String) : Failure()
    class TimeOutError(override var message: String?) : Failure()
    object NoConnectivityError : Failure()
    object EmptyResponse : Failure()
    object IgnorableError : Failure()
}