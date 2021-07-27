package ru.samitin.viewmodel.model.repository

import ru.samitin.viewmodel.model.data.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather

}