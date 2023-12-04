package com.hwido.pieceofdayfront.writeNew

interface WeatherCallback {
    fun onSuccessWeather(weatherCategory: String)
    fun onErrorWeather(weatherError: Throwable)
}