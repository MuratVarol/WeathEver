package com.varol.weathever.screen.weather

import com.varol.weathever.R
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentWeatherBinding

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>(){

    override val layoutId: Int
        get() = R.layout.fragment_weather
}