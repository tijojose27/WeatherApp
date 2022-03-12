package com.example.weatherapp.networking

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    suspend fun getWeatherForCity(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<WeatherModel>

}