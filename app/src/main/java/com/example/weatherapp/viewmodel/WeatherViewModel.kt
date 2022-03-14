package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.helper.WeatherHelpers
import com.example.weatherapp.helper.WeatherType
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.networking.RetrofitInstance
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    var cityLiveData = MutableLiveData("---------")
    var tempLiveData = MutableLiveData("-.-\u2109")
    var descLiveData = MutableLiveData("--------")


    var errorLiveData = MutableLiveData(false)
    var errorMessage = MutableLiveData("Something is not right. Try Again")



    init {

        when (WeatherHelpers.weatherType) {
            WeatherType.CITY -> {
                val city = WeatherHelpers.city
                getCityWeather(city)
            }
            WeatherType.ZIP -> {
                val currZip = WeatherHelpers.zip
                getZipWeather(currZip)
            }
            else -> {
                val currLat = WeatherHelpers.lat
                val currLon = WeatherHelpers.lon
                getLocationWeather(currLat, currLon)
            }
        }

    }

    private fun getCityWeather(city: String?) {
        if (!city.isNullOrEmpty()) {
            viewModelScope.launch {

                val response = try {
                    RetrofitInstance.api.getWeatherForCity(
                        city = city
                    )
                } catch (e: Exception) {
                    errorLiveData.value = true
                    Log.e("Exception", "$e")
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    getCityAndTemp(response)

                } else if (response.errorBody() != null) {
                    getErrorMessage(response)
                    return@launch
                } else
                    errorLiveData.value = true
                return@launch

            }
        }else{
            errorMessage.value = "city is blank"
        }
    }

    private fun getZipWeather(currZip: String?) {
        if (!currZip.isNullOrEmpty()) {
            viewModelScope.launch {

                val response = try {
                    RetrofitInstance.api.getWeatherForZip(
                        zip = currZip
                    )
                } catch (e: Exception) {
                    errorLiveData.value = true
                    Log.e("Exception", "$e")
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    getCityAndTemp(response)

                } else if (response.errorBody() != null) {
                    getErrorMessage(response)
                    return@launch
                } else
                    errorLiveData.value = true
                return@launch

            }
        }else{
            errorMessage.value = "ZIP is blank"
        }
    }

    private fun getLocationWeather(currLat: Double?, currLon: Double?) {
        viewModelScope.launch {
            val response = try {
                RetrofitInstance.api.getWeatherForLoc(
                    lat = currLat.toString(),
                    lon = currLon.toString()
                )
            }
            catch (e: Exception) {
                errorLiveData.value = true
                Log.e("Exception", "$e")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                getCityAndTemp(response)

            } else if (response.errorBody() !=null) {
                getErrorMessage(response)
                return@launch
            }else
                errorLiveData.value = true
            return@launch
        }
    }

    fun getErrorMessage(response: Response<WeatherModel>) {
        val error = response.errorBody()?.string()
        val message = JsonParser().parse(error)
            .asJsonObject["message"]
            .asString
        errorMessage.value = message
        errorLiveData.value = true
    }

    fun getCityAndTemp(response: Response<WeatherModel>) {
        val city = response.body()!!.name
        val temp = response.body()!!.main.temp.toString() + "\u2109"
        val desc = response.body()!!.weather[0].description
        cityLiveData.value = city
        tempLiveData.value = temp
        descLiveData.value = desc
    }
}