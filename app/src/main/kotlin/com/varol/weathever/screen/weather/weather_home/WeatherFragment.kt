package com.varol.weathever.screen.weather.weather_home

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.varol.weathever.R
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentWeatherBinding
import com.varol.weathever.internal.extension.showPopup
import com.varol.weathever.internal.extension.showToast
import com.varol.weathever.internal.listeners.ToolbarListener
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel
import com.varol.weathever.screen.weather.WeatherViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.RequestExecutor

const val LOCATION_REQUEST_CODE = 1001

object WeatherDirections {
    val toWeatherList = WeatherFragmentDirections.actionWeatherFragmentToWeatherListFragment()
}

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_weather

    private val toolbarCallback = object : ToolbarListener {
        override fun onRightIconClicked() {
            viewModel.toSavedWeatherList()
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationManager: LocationManager? by lazy {
        context?.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun initialize() {
        binder.apply {
            callback = toolbarCallback
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // we have the data, so we have loc.
        // no need to set loc listener and fetch data on return of list fragment
        if (viewModel.currentLocWeather.value != null)
            return

        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.ACCESS_FINE_LOCATION)
            .rationale { _, _, executor ->
                showNeedLocationPermissionDialog(executor)
            }
            .onGranted {
                setLocationListenerIfGpsEnabled()
            }
            .onDenied {
                showEnablePermissionInSettingsDialog()
            }.start()
    }

    private fun setLocationListenerIfGpsEnabled() {
        if (locationManager?.isLocationEnabled == true) {
            setLocationListener()
        } else {
            showEnableGPSDialog()
        }
    }

    private fun setLocationListener() {
        activity?.let { activity ->
            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(activity).apply {
                    lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            context.showToast(it.latitude.toString() + " " + location.longitude.toString())
                            viewModel.getWeatherByLocation(
                                it.latitude,
                                it.longitude
                            )
                        }
                    }
                }
        } ?: kotlin.run {
            viewModel.showErrorBar("Oops so strange!, Maybe restart app?")
        }
    }

    private fun showNeedLocationPermissionDialog(executor: RequestExecutor) {
        showPopup(
            PopupUiModel(
                message = "İzin veriniz!",
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    executor.execute()
                }

                override fun onCancelClick() {
                    executor.cancel()
                }
            }
        )
    }

    private fun showEnableGPSDialog() {
        showPopup(
            PopupUiModel(
                message = "GPS ayarlarını açın!",
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        LOCATION_REQUEST_CODE
                    )
                }

                override fun onCancelClick() {
                    viewModel.showInformBar("What can I do more to make you enable GPS?")
                }
            }
        )
    }

    private fun showEnablePermissionInSettingsDialog() {
        showPopup(
            PopupUiModel(
                message = "Enable Permission in settings screen for location",
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    startInstalledAppDetailsActivity()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // Android Pie returns RESULT_CANCELLED even user enable GPS, no need to check for resultCode
            setLocationListener()
        }
    }

}