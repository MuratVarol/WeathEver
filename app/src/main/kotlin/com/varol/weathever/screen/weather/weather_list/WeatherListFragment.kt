package com.varol.weathever.screen.weather.weather_list

import com.varol.weathever.R
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentSavedWeathersBinding
import com.varol.weathever.screen.weather.WeatherViewModel


class WeatherListFragment : BaseFragment<WeatherViewModel, FragmentSavedWeathersBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_saved_weathers

    override fun initialize() {
        viewModel.getWeathersFromDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.timerDisposableComposite.clear()
        viewModel.resetProgressStatus()
    }

}