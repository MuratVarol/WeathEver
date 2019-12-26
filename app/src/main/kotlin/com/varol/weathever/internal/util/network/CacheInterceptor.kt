package com.varol.weathever.internal.util.network

import okhttp3.Interceptor
import okhttp3.Response

private const val CACHE_LIFE = 10 //seconds

/**
 *  If the cache is older than {CACHE_LIFE} seconds, then discard it,
 *  and indicate an error in fetching the response.
 *  The 'max-age' attribute is responsible for this behavior.
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.newBuilder().header("Cache-Control", "public, max-age=$CACHE_LIFE").build()
        return chain.proceed(request)
    }
}
