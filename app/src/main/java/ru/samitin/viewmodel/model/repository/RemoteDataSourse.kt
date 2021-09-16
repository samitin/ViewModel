package ru.samitin.viewmodel.model.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.samitin.viewmodel.BuildConfig
import ru.samitin.viewmodel.model.dto.WeatherDTO


class RemoteDataSourse {

    private val weatherAPI=Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        ).build().create(WeatherAPI::class.java)

    fun getWeatherDetails(lat:Double,lon:Double,callBack: Callback<WeatherDTO>){
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(callBack)

    }


}