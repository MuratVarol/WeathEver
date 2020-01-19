package com.varol.weathever.screen.weather.search

import com.varol.weathever.R
import com.varol.weathever.base.BaseFragment
import com.varol.weathever.databinding.FragmentSavedWeathersBinding
import com.varol.weathever.screen.weather.WeatherViewModel


class SearchFragment : BaseFragment<WeatherViewModel, FragmentSavedWeathersBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_saved_weathers

    override fun initialize() {
        viewModel.getWeathersFromDb()
    }

}