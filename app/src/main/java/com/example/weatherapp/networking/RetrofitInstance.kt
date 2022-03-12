package com.example.weatherapp.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val baseURL = "https://api.openweathermap.org/data/2.5/"


    val api: WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

}