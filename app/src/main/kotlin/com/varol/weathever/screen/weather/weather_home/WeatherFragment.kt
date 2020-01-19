package com.varol.weathever.screen.weather.weather_home

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.varol.weathever.R
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentWeatherBinding
import com.varol.weathever.internal.extension.hideKeyboard
import com.varol.weathever.internal.extension.showPopup
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

    override fun initialize() {
        binder.apply {
            callback = toolbarCallback
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binder.cvLocation.setOnClickListener {
            binder.tvSearch.hideKeyboard()
            viewModel.searchText.value?.let { query ->
                if (query.isNotEmpty()) {
                    viewModel.getWeatherByCityName(query)
                } else {
                    checkLocationPermissionAndFetchData(true)
                }
            }
        }
        checkLocationPermissionAndFetchData()
    }

    private fun checkLocationPermissionAndFetchData(forceUpdate: Boolean = false) {
        //TODO: this is not best approach but works for now, fix it with better approach later
        // we have the data, so we have loc.
        // no need to set loc listener and fetch data on return of list fragment
        if (viewModel.currentLocWeather.value != null && !forceUpdate)
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
        if (isLocationEnabled()) {
            setLocationListener()
        } else {
            showEnableGPSDialog()
        }
    }

    private fun setLocationListener() {
        activity?.let { activity ->
            viewModel.showLocationWaitingProgress()
            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(activity).apply {
                    lastLocation.addOnSuccessListener { location: Location? ->
                        viewModel.hideLocationWaitingProgress()
                        location?.let {
                            viewModel.getWeatherByLocation(it.latitude, it.longitude)
                        } ?: run {
                            viewModel.showErrorBar(context?.getString(R.string.error_location_can_not_be_gathered).toString())
                        }
                    }
                }
        } ?: kotlin.run {
            viewModel.showErrorBar(context?.getString(R.string.error_wtf_error).toString())
        }
    }

    private fun showNeedLocationPermissionDialog(executor: RequestExecutor) {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.need_location_permission),
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
                message = context?.getString(R.string.please_enable_gps),
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
                    viewModel.showInformBar(context?.getString(R.string.no_option_left_to_enable_gps).toString())
                }
            }
        )
    }

    private fun showEnablePermissionInSettingsDialog() {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.please_enable_gps),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    startInstalledAppDetailsActivity()
                }

                override fun onCancelClick() {
                    showUnableToInitLocationManagerDialog()
                }
            }
        )
    }

    private fun showUnableToInitLocationManagerDialog() {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.do_you_want_to_locate_sydney),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    viewModel.getWeatherByLocation(
                        -33.8568022,
                        151.2143847
                    )
                }

                override fun onCancelClick() {
                    viewModel.showErrorBar(context?.getString(R.string.is_device_has_gps).toString())
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