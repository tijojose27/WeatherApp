package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.networking.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var cityLiveData = MutableLiveData("")
    var tempLiveData = MutableLiveData("-.-")
    var errorLiveData = MutableLiveData(false)

    init {
        viewModelScope.launch {
            val response = try {
                RetrofitInstance.api.getWeatherForCity(
                    lat = lat,
                    lon = long,
                    appId = appId,
                    units = units
                )
            } catch (e: Exception) {
                errorLiveData.value = true

                Log.e("Exception", "$e")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val city = response.body()!!.name
                val temp = response.body()!!.main.temp.toString()
                cityLiveData.value = city
                tempLiveData.value = temp

            } else {
                errorLiveData.value = true
                return@launch
            }
        }
    }

    companion object {
        var appId = "f2259ea9d6e1e64fef69d6fd66bd0984"
        var lat = "29.760427"
        var long = "-95.369804"
        var units = "imperial"
    }

}