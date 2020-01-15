package com.varol.weathever.screen.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.varol.weathever.base.BaseAndroidViewModel
import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.domain.GetLocationWeatherUseCase
import com.varol.weathever.domain.SveCurrentWeatherToDbUseCase
import com.varol.weathever.internal.util.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class WeatherViewModel @Inject constructor(
    context: Context,
    private val weatherByLoc: GetLocationWeatherUseCase,
    private val saveWeather: SveCurrentWeatherToDbUseCase
) : BaseAndroidViewModel(context) {

    val currentLocWeather = MutableLiveData<WeatherViewEntity>()
    val isSaveButtonEnabled = SingleLiveData<Boolean>().apply { value = false }
    val progress = MutableLiveData<Int>()
    private val timerDisposableComposite = CompositeDisposable()

    fun getWeatherByLocationPeriodically(
        lat: Double,
        lon: Double,
        interval: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        timerToLoadIdleFragment(interval, timeUnit)
        getWeatherByLocation(lat, lon)
        disposables.add(
            startCountdown(interval, timeUnit)
                .subscribe {
                    getWeatherByLocation(lat, lon)
                })
    }

    private fun getWeatherByLocation(lat: Double, lon: Double) {
        showProgress()
        disposables.add(
            weatherByLoc.getWeatherByLocation(lat, lon)
                .subscribe { value -> value.either(::handleFailure, ::handleSuccessGetLocWeather) }
        )
    }

    private fun handleSuccessGetLocWeather(weatherViewEntity: WeatherViewEntity) {
        hideProgress()
        reFireTimerForProgress()
        currentLocWeather.postValue(weatherViewEntity)
        isSaveButtonEnabled.postValue(true)
    }

    private fun reFireTimerForProgress() {
        timerDisposableComposite.clear()
        timerToLoadIdleFragment(PERIODIC_FETCH_INTERVAL)
    }

    private fun timerToLoadIdleFragment(
        countDownTime: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        progress.postValue(100)
        val multiplier = 100.div(countDownTime.toDouble())
        var tickCount: Int = countDownTime.toInt()
        val disposable = startTimer(1, timeUnit, countDownTime).subscribe {
            progress.postValue((--tickCount * multiplier).toInt())
        }
        timerDisposableComposite.add(disposable)
    }

    fun saveCurrentWeatherToDb() {
        currentLocWeather.value?.let { weather ->
            showProgress()
            disposables.add(
                saveWeather.saveWeatherToDb(weather)
                    .observeOn(getMainThreadScheduler())
                    .subscribeOn(getBackgroundScheduler())
                    .subscribe({ rowIndex ->
                        hideProgress()
                        showInformBar("Saved")
                    }, { ex ->
                        showErrorBar(ex.localizedMessage)
                    })
            )
        } ?: run {
            showErrorBar("Hmm, Current weather seems null")
        }
    }
}