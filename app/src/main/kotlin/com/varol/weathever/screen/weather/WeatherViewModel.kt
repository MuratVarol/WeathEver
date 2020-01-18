package com.varol.weathever.screen.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.varol.weathever.base.BaseAndroidViewModel
import com.varol.weathever.data.entity.WeatherListItemViewEntity
import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.domain.*
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel
import com.varol.weathever.internal.util.SingleLiveData
import com.varol.weathever.screen.weather.weather_home.WeatherDirections
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val CONST_HUNDRED = 100

const val PERIODIC_FETCH_INTERVAL = 15L // a minute

class WeatherViewModel @Inject constructor(
    context: Context,
    private val weatherByLoc: GetLocationWeatherUseCase,
    private val saveWeather: SaveCurrentWeatherToDbUseCase,
    private val savedWeathers: GetSavedWeatherListUseCase,
    private val weatherByCityIdRemote: GetWeatherByCityIdRemoteUseCase,
    private val updateWeather: UpdateWeatherOnDbUseCase,
    private val weatherByCityIdLocal: GetWeatherByCityIdLocalUseCase,
    private val deleteWeather: DeleteWeatherFromDbUseCase
) : BaseAndroidViewModel(context) {

    val currentLocWeather = MutableLiveData<WeatherViewEntity>()
    val isSaveButtonEnabled = SingleLiveData<Boolean>()
        .apply { value = false }
    val progress = MutableLiveData<Int>()
    val savedWeatherList = MutableLiveData<MutableList<WeatherListItemViewEntity>>()
    val remainingSeconds = MutableLiveData<String>()
    val timerDisposableComposite = CompositeDisposable()

    fun getWeatherByLocation(lat: Double, lon: Double) {
        showProgress()
        disposables.add(
            weatherByLoc.getWeatherByLocation(lat, lon)
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

    fun resetProgressStatus() {
        progress.postValue(CONST_HUNDRED)
        remainingSeconds.postValue(PERIODIC_FETCH_INTERVAL.toString())
    }

    fun deleteWeatherItemFromDb(weatherItem: WeatherListItemViewEntity) {
        navigate(
            PopupUiModel(
                message = "${weatherItem.city} will be deleted!\nAre you sure?",
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
                    showInformBar("${weather.city} deleted successfully")
                }, {
                    hideProgress()
                    showErrorBar("${weather.city} can not delete from DB")
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
                    showInformBar("Updated Weather saved to DB")
                }, {
                    showErrorBar("Updated Weather can not be saved")
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
                        showInformBar("Saved")
                    }, { ex ->
                        showErrorBar(ex.localizedMessage)
                    })
            )
            hideProgress()
        } ?: run {
            showErrorBar("Hmm, Current weather seems null")
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
                    showInformBar(weather.cityName.toString())
                }, { ex ->
                    showErrorBar(ex.localizedMessage)
                })

        )

    }
}