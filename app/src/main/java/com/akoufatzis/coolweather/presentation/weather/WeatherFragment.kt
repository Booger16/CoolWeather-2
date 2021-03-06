package com.akoufatzis.coolweather.presentation.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.akoufatzis.coolweather.R
import com.akoufatzis.coolweather.databinding.FragmentWeatherBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val weatherViewModel: WeatherViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel::class.java)
    }

    private val weatherAdapter = WeatherAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherViewModel.viewState.observe(viewLifecycleOwner, Observer {
            if (it.data != null) {
                val item = WeatherItem(
                    it.data.city.name,
                    it.data.weather.celsiusDegrees(context!!),
                    it.data.weather.iconResource()
                )
                weatherAdapter.submitList(listOf(item))
            }
        })
        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.inflate<FragmentWeatherBinding>(inflater, R.layout.fragment_weather, container, false)
        binding.rvWeather.adapter = weatherAdapter
        binding.rvWeather.layoutManager = LinearLayoutManager(context)
        binding.rvWeather.setHasFixedSize(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        weatherViewModel.showWeather("Thessaloniki")
    }
}
