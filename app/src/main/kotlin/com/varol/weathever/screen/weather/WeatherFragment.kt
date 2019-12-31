package com.varol.weathever.screen.weather

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.varol.weathever.R
import com.varol.weathever.base.BaseBindingActivity
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentWeatherBinding
import com.varol.weathever.internal.extension.showPopup
import com.varol.weathever.internal.extension.showToast
import com.varol.weathever.internal.popup.PopupCallback
import com.varol.weathever.internal.popup.PopupUiModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.RequestExecutor

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_weather

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale { _, _, executor ->
                    showNeedLocationPermissionDialog(executor)
                }
                .onGranted {
                    setLocationListener(activity)
                }
                .onDenied {
                    showEnablePermissionInSettingsDialog()
                }
                .start()
        }
    }

    private fun setLocationListener(activity: FragmentActivity) {
        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(activity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        context.showToast(it.latitude.toString() + " " + location.longitude.toString())
                        viewModel.getWeatherByLocation(it.latitude, it.longitude)
                    }
                }
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

}