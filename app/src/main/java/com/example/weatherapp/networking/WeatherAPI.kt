package com.example.weatherapp.networking

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object{
        const val APPID = "f2259ea9d6e1e64fef69d6fd66bd0984"
        const val UNITS = "imperial"
    }

    @GET("weather")
    suspend fun getWeatherForLoc(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String = APPID,
        @Query("units") units: String = UNITS
    ): Response<WeatherModel>



    @GET("weather")
    suspend fun getWeatherForCity(
        @Query("q") city: String,
        @Query("appid") appId: String = APPID,
        @Query("units") units: String = UNITS
    ): Response<WeatherModel>

    @GET("weather")
    suspend fun getWeatherForZip(
        @Query("zip") zip: String,
        @Query("appid") appId: String = APPID,
        @Query("units") units: String = UNITS
    ): Response<WeatherModel>

}