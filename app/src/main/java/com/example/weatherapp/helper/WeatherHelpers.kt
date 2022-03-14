package com.example.weatherapp.helper

enum class WeatherType{
    LOCATION,
    CITY,
    ZIP
}


object WeatherHelpers {

    var weatherType: WeatherType? = null

    var lat: Double? = null
    var lon: Double? = null

    var city: String? = null

    var zip: String? = null

    fun updateLatLong(currLat: Double, currLon: Double){

        clearData()

        weatherType = WeatherType.LOCATION
        lat = currLat
        lon = currLon
    }

    fun updateCity(currCity: String){
        clearData()
        weatherType = WeatherType.CITY
        city = currCity
    }

    fun updateZip(currZip: String){
        clearData()

        weatherType = WeatherType.ZIP
        zip = currZip
    }

    fun clearData(){
        city = null
        zip = null

        weatherType = null
        lat = null
        lon = null
    }


}