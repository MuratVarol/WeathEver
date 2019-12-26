package com.varol.weathever.internal.util.network

import com.varol.weathever.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

private const val API_KEY_QUERY = "appid"
private const val KEY_UNIT = "units"
private const val VALUE_UNIT = "metric"
private const val KEY_LANG = "lang"


/**
 * OkHttp interceptor for providing API_KEY to API requests for every requests.
 */
class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val apiKeyValue = BuildConfig.API_KEY
        var request = chain.request()
        val httpUrl = request.url().newBuilder()
            .addQueryParameter(API_KEY_QUERY, apiKeyValue)
            .addQueryParameter(KEY_UNIT, VALUE_UNIT)
            .addQueryParameter(KEY_LANG, Locale.getDefault().language)
            .build()
        request = request.newBuilder().url(httpUrl).build()

        return chain.proceed(request)
    }
}
