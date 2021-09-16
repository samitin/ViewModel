package ru.samitin.viewmodel.model.repository

import ru.samitin.viewmodel.model.dto.WeatherDTO


interface DetailsRepository {

    fun getWeatherDetailsFromServer(       lat: Double,
                                           lon: Double,
                                           callback: retrofit2.Callback<WeatherDTO>)

}