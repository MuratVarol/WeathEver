package com.varol.weathever.screen.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.varol.weathever.R
import com.varol.weathever.base.BaseAndroidViewModel
import com.varol.weathever.data.entity.WeatherListItemViewEntity
import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.domain.*
import com.varol.weathever.internal.extension.EMPTY
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel
import com.varol.weathever.internal.util.SingleLiveData
import com.varol.weathever.screen.weather.weather_home.WeatherDirections
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val CONST_HUNDRED = 100

const val PERIODIC_FETCH_INTERVAL = 60L // a minute

class WeatherViewModel @Inject constructor(
    context: Context,
    private val weatherByLoc: GetLocationWeatherUseCase,
    private val saveWeather: SaveCurrentWeatherToDbUseCase,
    private val savedWeathers: GetSavedWeatherListUseCase,
    private val weatherByCityIdRemote: GetWeatherByCityIdRemoteUseCase,
    private val updateWeather: UpdateWeatherOnDbUseCase,
    private val weatherByCityIdLocal: GetWeatherByCityIdLocalUseCase,
    private val deleteWeather: DeleteWeatherFromDbUseCase,
    private val weatherByCityName: GetWeatherByCityNameUseCase
) : BaseAndroidViewModel(context) {

    val currentLocWeather = MutableLiveData<WeatherViewEntity>()
    val isSaveButtonEnabled = SingleLiveData<Boolean>()
    val isWaitingForLocation = SingleLiveData<Boolean>()
        .apply { value = false }
    val progress = MutableLiveData<Int>()
    val isProgressVisible = MutableLiveData<Boolean>().apply { value = true }
    val searchText = MutableLiveData<String>().apply { value = String.EMPTY }
    val savedWeatherList = MutableLiveData<MutableList<WeatherListItemViewEntity>>()
    val remainingSeconds = MutableLiveData<String>()

    val timerDisposableComposite = CompositeDisposable()

    fun getWeatherByLocation(lat: Double, lon: Double) {
        showProgress()
        resetProgressStatus()
        disposables.add(
            weatherByLoc.getWeatherByLocation(lat, lon)
                .subscribe { value -> value.either(::handleFailure, ::handleSuccessGetLocWeather) }
        )
    }

    fun getWeatherByCityName(query: String) {
        showProgress()
        cancelTimer()
        disposables.add(
            weatherByCityName.getWeatherByCityName(query)
                .subscribe { value -> value.either(::handleFailure, ::handleSuccessGetLocWeather) }
        )
    }

    private fun handleSuccessGetLocWeather(weatherViewEntity: WeatherViewEntity) {
        hideProgress()
        setNextRefreshTimer(
            weatherViewEntity.coordinates.lat,
            weatherViewEntity.coordinates.lon,
            PERIODIC_FETCH_INTERVAL
        )
        currentLocWeather.postValue(weatherViewEntity)
        isSaveButtonEnabled.postValue(true)
    }

    private fun setNextRefreshTimer(
        lat: Double,
        lon: Double,
        countDownTime: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        val multiplier = CONST_HUNDRED.div(countDownTime.toDouble())
        var tickCount: Int = countDownTime.toInt()
        val disposable = startTimer(1, timeUnit, countDownTime).subscribe {
            remainingSeconds.postValue((--tickCount).toString())
            progress.postValue((tickCount * multiplier).toInt())
            if (tickCount <= 0) {
                getWeatherByLocation(lat, lon)
                resetProgressStatus()
            }
        }
        timerDisposableComposite.clear()
        timerDisposableComposite.add(disposable)
    }

    private fun cancelTimer() {
        isProgressVisible.postValue(false)
        timerDisposableComposite.clear()
    }

    fun resetProgressStatus() {
        progress.postValue(CONST_HUNDRED)
        remainingSeconds.postValue(PERIODIC_FETCH_INTERVAL.toString())
        isProgressVisible.postValue(true)
    }

    fun deleteWeatherItemFromDb(weatherItem: WeatherListItemViewEntity) {
        navigate(
            PopupUiModel(
                message = context.getString(R.string.are_sure_delete_with_param, weatherItem.city),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    deleteWeatherFromDbByCityId(weatherItem)
                }
            }
        )
    }

    fun deleteWeatherFromDbByCityId(weather: WeatherListItemViewEntity) {
        showProgress()
        disposables.add(
            deleteWeather.deleteWeatherFromDbByCityId(weather.id)
                .observeOn(getMainThreadScheduler())
                .subscribeOn(getBackgroundScheduler())
                .subscribe({
                    hideProgress()
                    showInformBar(
                        context.getString(
                            R.string.city_deleted_successfully_with_param,
                            weather.city
                        )
                    )
                }, {
                    hideProgress()
                    showErrorBar(
                        context.getString(
                            R.string.city_deleted_failed_with_param,
                            weather.city
                        )
                    )
                })
        )
    }

    fun refreshWeatherItem(id: Long) {
        showProgress()
        disposables.add(
            weatherByCityIdRemote.getWeatherByCityId(id)
                .subscribe { value -> value.either(::handleFailure, ::handleUpdatedWeather) }
        )
    }

    private fun handleUpdatedWeather(weatherViewEntity: WeatherViewEntity) {
        hideProgress()
        disposables.add(
            updateWeather.updateWeatherOnDb(weatherViewEntity)
                .observeOn(getMainThreadScheduler())
                .subscribeOn(getBackgroundScheduler())
                .subscribe({
                    showInformBar(
                        context.getString(
                            R.string.weather_updated_and_saved,
                            weatherViewEntity.cityName
                        )
                    )
                }, {
                    showErrorBar(context.getString(R.string.weather_updated_save_failed))
                })
        )
    }

    fun saveCurrentWeatherToDb() {
        currentLocWeather.value?.let { weather ->
            showProgress()
            disposables.add(
                saveWeather.saveWeatherToDb(weather)
                    .observeOn(getMainThreadScheduler())
                    .subscribeOn(getBackgroundScheduler())
                    .subscribe({
                        showInformBar(
                            context.getString(
                                R.string.city_saved_with_param,
                                weather.cityName
                            )
                        )
                    }, { ex ->
                        showErrorBar(ex.localizedMessage)
                    })
            )
            hideProgress()
        } ?: run {
            showErrorBar(context.getString(R.string.null_weather))
        }
    }

    fun getWeathersFromDb() {
        showProgress()
        disposables.add(
            savedWeathers.getSavedWeathers()
                .observeOn(getMainThreadScheduler())
                .subscribeOn(getBackgroundScheduler())
                .subscribe({ savedWeathers ->
                    savedWeatherList.postValue(savedWeathers.toMutableList())
                }, { ex ->
                    showErrorBar(ex.localizedMessage)
                })
        )
        hideProgress()
    }

    fun toSavedWeatherList() {
        navigate(WeatherDirections.toWeatherList)
    }

    fun onWeatherSelect(cityId: Long) {
        showProgress()
        disposables.add(
            weatherByCityIdLocal.getWeatherByCityId(cityId)
                .observeOn(getMainThreadScheduler())
                .subscribeOn(getBackgroundScheduler())
                .subscribe({ weather ->
                    hideProgress()
                    showInformBar(weather.cityName)
                }, { ex ->
                    showErrorBar(ex.localizedMessage)
                })

        )
    }

    fun showLocationWaitingProgress() {
        showProgress()
        isWaitingForLocation.postValue(true)
    }

    fun hideLocationWaitingProgress() {
        hideProgress()
        isWaitingForLocation.postValue(false)
    }
}