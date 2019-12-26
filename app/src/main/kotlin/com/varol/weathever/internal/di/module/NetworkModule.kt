package com.varol.weathever.internal.di.module

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varol.weathever.BuildConfig
import com.varol.weathever.internal.util.network.CacheInterceptor
import com.varol.weathever.internal.util.network.MoshiConverters
import com.varol.weathever.internal.util.network.RequestInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val CLIENT_TIME_OUT = 120L
private const val CLIENT_CACHE_SIZE = 10 * 1024 * 1024L
private const val CLIENT_CACHE_DIRECTORY = "http"

@Module
class NetworkModule {

    /**
     * Create Cache object for OkHttp instance
     */
    @Provides
    @Singleton
    fun createCache(context: Context): Cache = Cache(
        File(
            context.cacheDir,
            CLIENT_CACHE_DIRECTORY
        ),
        CLIENT_CACHE_SIZE
    )

    @Provides
    @Singleton
    fun createMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Wrapped.ADAPTER_FACTORY)
            .add(MoshiConverters())
            .build()
    }

    /**
     * returns ChuckInterceptor
     * ChuckInterceptor: API request-response interceptor for debugging API traffic
     */
    @Provides
    @Singleton
    fun createChuckInterceptor(context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    /**
     * returns CacheInterceptor
     */
    @Provides
    @Singleton
    fun createCacheInterceptor(): CacheInterceptor {
        return CacheInterceptor()
    }

    /**
     * returns RequestInterceptor
     */
    @Provides
    @Singleton
    fun createRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    /**
     * Create OkHttp client with required interceptors and defined timeouts
     */
    @Provides
    @Singleton
    fun createOkHttpClient(
        cache: Cache,
        chuckInterceptor: ChuckInterceptor,
        cacheInterceptor: CacheInterceptor,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder
            .addInterceptor(chuckInterceptor)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(requestInterceptor)
            .connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .cache(cache)
        return okHttpBuilder.build()
    }

    /**
     * returns URL for API
     */
    @Provides
    @Singleton
    fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }


    /**
     * Create Retrofit Client
     *
     * <reified T> private func let us using reflection.
     * We can use generics and reflection so ->
     *  we don't have to define required Api Interface here
     */
    @Provides
    @Singleton
    inline fun <reified T> createWebService(
        okHttpClient: Lazy<OkHttpClient>,
        moshi: Moshi,
        baseUrl: String
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory { okHttpClient.get().newCall(it) }
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(T::class.java)
    }
}